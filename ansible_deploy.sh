#!/bin/bash

# This script requires the specified user to have sudo and ssh access
DEPLOY_DIR="${PWD}/deploy"
HOSTS_FILE="${DEPLOY_DIR}/hosts"
DEPLOY_USER="deployer"

if [ ! -e "$HOSTS_FILE" ]; then
    echo "No hosts file found, you may need to rename ${HOSTS_FILE}.dist"
    exit
fi

gradle clean bootRepackage

ansible-playbook -i ${HOSTS_FILE} ${DEPLOY_DIR}/ansible/playbook.yml -u ${DEPLOY_USER} -s -K