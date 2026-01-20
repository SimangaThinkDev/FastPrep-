#!/bin/bash

set -e

echo "--- Updating system packages ---"
sudo apt update && sudo apt upgrade -y

echo "--- Installing dependencies ---"
sudo apt install -y gnupg curl apt-transport-https ca-certificates

echo "--- Importing MongoDB GPG Key ---"
curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | \
   sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg \
   --dearmor --yes

echo "--- Creating Repository List ---"
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/debian bookworm/mongodb-org/7.0 main" | \
   sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

echo "--- Installing MongoDB ---"
sudo apt update
sudo apt install -y mongodb-org

echo "--- Starting and Enabling MongoDB Service ---"
sudo systemctl start mongod
sudo systemctl enable mongod

echo "--- Installation Complete ---"
sudo systemctl status mongod --no-pager