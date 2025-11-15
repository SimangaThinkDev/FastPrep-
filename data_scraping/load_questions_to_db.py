import re
import json

def parse_markdown_quiz(markdown_content: str, exam_id: int):
    """
    Parses the Markdown exam content into the specified JSON structure.

    Args:
        markdown_content (str): The raw text content of the markdown file.

    Returns:
        dict: A dictionary conforming to the desired JSON structure.
    """

    # 1. Extract YAML Front Matter (Metadata) and Title

    # Regex for YAML block: finds content between '---' lines at the start
    metadata_match = re.match(r"---\s*\n(.*?)\n---", markdown_content, re.DOTALL)

    metadata = {}
    remaining_content = markdown_content

    if metadata_match:
        yaml_block = metadata_match.group(1).strip()
        remaining_content = markdown_content[metadata_match.end() :].strip()

        for line in yaml_block.split("\n"):
            if ":" in line:
                key, value = line.split(":", 1)
                metadata[key.strip()] = value.strip()

    # Extract the main title (e.g., "# Practice Exam 1")
    title_match = re.search(r"^#\s*(.+)", remaining_content, re.MULTILINE)
    if title_match:
        metadata["title"] = title_match.group(1).strip()
        # Remove the title line from remaining content
        remaining_content = remaining_content[title_match.end() :].strip()

    # Default/Placeholder exam ID, since the markdown doesn't provide one
    metadata["exam_id"] = exam_id

    # 2. Split Content into Individual Question Blocks
    # Split by the question number followed by a period (e.g., "1. ", "2. ")
    # The first element is usually junk (whitespace/newlines), so we skip it.
    question_blocks = re.split(r"\n(\d+\.\s)", remaining_content)[1:]

    questions_data = []

    # Iterate through question blocks (pairs of number text and block content)
    for i in range(0, len(question_blocks), 2):
        question_number = int(question_blocks[i].replace(".", "").strip())
        block_content = question_blocks[i + 1].strip()

        # 3. Extract Core Question Components

        parts = re.split(
            r"(<details.*Answer<\/summary>.*Correct Answer:\s*([\s\S]*?)(?:Explanation:|<\/details>))",
            block_content,
            1,
            re.DOTALL | re.IGNORECASE,
        )

        if len(parts) < 4:
            print(
                f"Warning: Skipping question {question_number} due to malformed block."
            )
            continue

        question_body = parts[0].strip()
        answer_keys_raw_messy = parts[2].strip()

        # Key Cleanup Logic

        cleaned_keys = re.sub(r"\s+", "", answer_keys_raw_messy).upper()

        correct_answer_keys = re.sub(r"[^A-Z,]", "", cleaned_keys)

        if len(correct_answer_keys) > 1 and "," not in correct_answer_keys:
            correct_answer_keys = ",".join(list(correct_answer_keys))

        # Split body into question text and options
        text_and_options = re.split(
            r"^\s*-\s*[A-Z]\.\s", question_body, 1, re.MULTILINE
        )

        if len(text_and_options) < 2:
            print(
                f"Warning: Skipping question {question_number} due to missing options."
            )
            continue

        question_text = text_and_options[0].strip()
        options_block = text_and_options[1].strip()

        options_list = re.split(
            r"^\s*-\s*([A-Z]\.\s)", options_block, flags=re.MULTILINE
        )

        current_options = []
        option_key_map = {}

        keys = ["A"] + [s.replace(".", "").strip() for s in options_list[1::2]]
        texts = [options_list[0]] + [s.strip() for s in options_list[2::2]]

        for key, text in zip(keys, texts):
            option = {"option_key": key, "option_text": text.strip()}
            current_options.append(option)
            option_key_map[key] = text.strip()

        correct_keys = [k.strip() for k in correct_answer_keys.split(",")]
        is_mc = len(correct_keys) > 1 or "(Choose" in question_text

        correct_texts = [
            option_key_map.get(key, "") for key in correct_keys if key in option_key_map
        ]

        correct_answer_text = " | ".join(correct_texts)

        questions_data.append(
            {
                "question_number": question_number,
                "question_text": question_text,
                "is_multiple_choice": is_mc,
                "correct_answer_keys": ", ".join(correct_keys),
                "correct_answer_text": correct_answer_text,
                "options": current_options,
            }
        )

    final_json = {"exam_metadata": metadata, "questions": questions_data}

    return final_json


def extract_num(filename):
    match = re.search(r"(\d+)", filename)
    return int(match.group(1)) if match else float("inf")


if __name__ == "__main__":
    import os

    target_folder_name = "exams_data_as_json"
    from_folder_name = "practice-exams"

    try:
        os.mkdir(target_folder_name)
    except FileExistsError:
        print("File already created!")

    exams = sorted(os.listdir("practice-exam"), key=extract_num)

    print("Exams: \n" + "".join(exam + "\n" for exam in exams))

    exam_id = 13

    for exam in exams:
        exam_path = from_folder_name + "/" + exam

        with open(exam_path, "r") as file:
            contents = file.read()

        parsed_data = parse_markdown_quiz(contents, exam_id)
        exam_id += 1

        output_path = target_folder_name + "/" + exam[:-3] + ".json"

        with open(output_path, "w", encoding="utf-8") as file2:
            json.dump(parsed_data, file2, indent=4)
