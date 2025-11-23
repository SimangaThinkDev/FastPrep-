#!/bin/bash

# Directory containing your ALREADY-FIXED exam JSON files
EXAM_DIR="./exams/AWS Certified Cloud Practitioner"

# MongoDB database and collection
DB_NAME="examdb"
COLLECTION_NAME="exams"

# Check if directory exists
if [[ ! -d "$EXAM_DIR" ]]; then
    echo "Error: Directory $EXAM_DIR does not exist."
    exit 1
fi

# Check if mongoimport is installed
if ! command -v mongoimport &> /dev/null; then
    echo "Error: mongoimport is not installed."
    exit 1
fi

for file in "$EXAM_DIR"/*.json; do
    [[ -f "$file" ]] || continue

    echo "----------------------------------------"
    echo "Importing $file ..."

    # Just ensure `_id` is removed so MongoDB can generate fresh IDs
    TMP_FILE=$(mktemp)
    jq 'del(._id)' "$file" > "$TMP_FILE"

    mongoimport \
        --db "$DB_NAME" \
        --collection "$COLLECTION_NAME" \
        --file "$TMP_FILE" \
        --upsert \
        --upsertFields exam_metadata.exam_id

    if [[ $? -eq 0 ]]; then
        echo "✔ Successfully imported $file"
    else
        echo "❌ Failed importing $file"
    fi

    rm -f "$TMP_FILE"
done
echo "----------------------------------------"

echo "ALL EXAMS IMPORTED SUCCESSFULLY!"
