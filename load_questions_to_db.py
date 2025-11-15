import re
import json

def parse_markdown_quiz(markdown_content:str, exam_id:int):
    """
    Parses the Markdown exam content into the specified JSON structure.
    
    Args:
        markdown_content (str): The raw text content of the markdown file.
        
    Returns:
        dict: A dictionary conforming to the desired JSON structure.
    """
    
    # 1. Extract YAML Front Matter (Metadata) and Title
    
    # Regex for YAML block: finds content between '---' lines at the start
    metadata_match = re.match(r'---\s*\n(.*?)\n---', markdown_content, re.DOTALL)
    
    metadata = {}
    remaining_content = markdown_content
    
    if metadata_match:
        yaml_block = metadata_match.group(1).strip()
        remaining_content = markdown_content[metadata_match.end():].strip()
        
        for line in yaml_block.split('\n'):
            if ':' in line:
                key, value = line.split(':', 1)
                metadata[key.strip()] = value.strip()
    
    # Extract the main title (e.g., "# Practice Exam 1")
    title_match = re.search(r'^#\s*(.+)', remaining_content, re.MULTILINE)
    if title_match:
        metadata['title'] = title_match.group(1).strip()
        # Remove the title line from remaining content
        remaining_content = remaining_content[title_match.end():].strip()

    # Default/Placeholder exam ID, since the markdown doesn't provide one
    metadata['exam_id'] = exam_id

    # 2. Split Content into Individual Question Blocks
    # Split by the question number followed by a period (e.g., "1. ", "2. ")
    # The first element is usually junk (whitespace/newlines), so we skip it.
    question_blocks = re.split(r'\n(\d+\.\s)', remaining_content)[1:]
    
    questions_data = []
    
    # Iterate through question blocks (pairs of number text and block content)
    for i in range(0, len(question_blocks), 2):
        question_number = int(question_blocks[i].replace('.', '').strip())
        block_content = question_blocks[i+1].strip()
        
        # 3. Extract Core Question Components
        
        # Split into question body (question text + options) and answer block
        parts = re.split(
            r'(<details.*Answer<\/summary>.*Correct Answer:\s*([\s\S]*?)(?:Explanation:|<\/details>))', 
            block_content, 1, re.DOTALL | re.IGNORECASE
        )

        if len(parts) < 4:
            # Handle malformed block, skip or log
            print(f"Warning: Skipping question {question_number} due to malformed block.")
            continue

        question_body = parts[0].strip()
        answer_keys_raw_messy = parts[2].strip() # This is the 'D' or 'B, E' part
        
        # --- Key Cleanup Logic ---

        # 1. Aggressively remove all whitespace (including newlines and tabs)
        cleaned_keys = re.sub(r'\s+', '', answer_keys_raw_messy).upper()

        # 2. Remove any remaining non-key, non-comma characters 
        # This final step ensures only A-Z and comma are present.
        correct_answer_keys = re.sub(r'[^A-Z,]', '', cleaned_keys)

        # Comma separates multi correct answers
        if len(correct_answer_keys) > 1 and ',' not in correct_answer_keys:
            correct_answer_keys = ','.join(list(correct_answer_keys))

        # Split question body into question text and options block
        # The options usually start with a hyphen/space followed by A.
        text_and_options = re.split(r'^\s*-\s*[A-Z]\.\s', question_body, 1, re.MULTILINE)
        
        if len(text_and_options) < 2:
            print(f"Warning: Skipping question {question_number} due to missing options.")
            continue

        question_text = text_and_options[0].strip()
        options_block = text_and_options[1].strip()

        # 4. Process Options
        # Split options block by subsequent options (e.g., "- B. ", "- C. ")
        options_list = re.split(r'^\s*-\s*([A-Z]\.\s)', options_block, flags=re.MULTILINE)
        
        # options_list will look like: [Content of A, 'B. ', Content of B, 'C. ', Content of C, ...]
        
        current_options = []
        option_key_map = {} # Map key (A, B) to text for easy lookup
        
        # The first element of options_list is the content of A, so we manually prep the list
        # using A as the first key.
        keys = ['A'] + [s.replace('.', '').strip() for s in options_list[1::2]]
        texts = [options_list[0]] + [s.strip() for s in options_list[2::2]]
        
        for key, text in zip(keys, texts):
            option = {
                "option_key": key,
                "option_text": text.strip()
            }
            current_options.append(option)
            option_key_map[key] = text.strip()

        # 5. Determine Correct Answer Text and Multi-Choice status
        
        correct_keys = [k.strip() for k in correct_answer_keys.split(',')]
        is_mc = len(correct_keys) > 1 or "(Choose" in question_text
        
        # Look up the text for the correct keys
        correct_texts = [option_key_map.get(key, '') for key in correct_keys if key in option_key_map]
        
        # Concatenate for the 'correct_answer_text' field
        correct_answer_text = " | ".join(correct_texts)

        # 6. Assemble Question Object
        questions_data.append({
            "question_number": question_number,
            "question_text": question_text,
            "is_multiple_choice": is_mc,
            "correct_answer_keys": ", ".join(correct_keys), # Store back as a comma-separated string
            "correct_answer_text": correct_answer_text,
            "options": current_options
        })

    # 7. Assemble Final JSON Structure
    final_json = {
        "exam_metadata": metadata,
        "questions": questions_data
    }
    
    return final_json

# To be used for sorting
def extract_num(filename):
    # Pull out the number before ".md"
    match = re.search(r'(\d+)', filename)
    return int(match.group(1)) if match else float('inf')


if __name__ == "__main__":
    # In a real environment, you would load the content from the file 'quiz.md'
    # For demonstration, we use a placeholder string.
    
    # --- PLACEHOLDER FOR FILE INPUT ---
    # Replace this section with code to read your file content:
    # try:
    #     with open('quiz.md', 'r', encoding='utf-8') as f:
    #         markdown_input = f.read()
    # except FileNotFoundError:
    #     print("Error: quiz.md not found.")
    #     exit()
    # -----------------------------------
    
    
    # NOTE: The full markdown content from the history must be used here for complete parsing.
    # Since I cannot access the full history, I'm using a placeholder but the logic is designed
    # to handle the full 50 questions as originally provided.

    import os

    target_folder_name = "exams_data_as_json"
    from_folder_name = "practice-exam"

    try:
        os.mkdir( target_folder_name )
    except FileExistsError:
        print( "File already created!" )
    
    exams = sorted( os.listdir( "practice-exam" ), key=extract_num )

    print( "Exams: \n" + "".join( exam + "\n" for exam in exams ) )

    exam_id = 13

    for exam in exams:

        exam_path = from_folder_name + "/" + exam
        
        with open( exam_path, "r" ) as file:
            contents =  file.read()

        parsed_data = parse_markdown_quiz(contents, exam_id )
        exam_id += 1 # Increment Immediately

        # Path to write the output to
        output_path = target_folder_name + "/" + exam[:-3] + ".json"

        with open( output_path, "w", encoding="utf-8" ) as file2:
            json.dump( parsed_data, file2, indent=4 )

    
    # # Output the JSON to the console
    # print(json.dumps(parsed_data, indent=4))

    # To save the output to a file:
    # with open('quiz_output.json', 'w', encoding='utf-8') as f:
    #     json.dump(parsed_data, f, indent=4)