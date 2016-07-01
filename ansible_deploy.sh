#!/bin/bash

# This script requires the specified user to have sudo and ssh access

DEPLOY_DIR="${PWD}/deploy"
HOSTS_FILE="${DEPLOY_DIR}/hosts"
DEPLOY_USER="deployer"
DEPLOY_FAT_JAR="y"
RUN_FILE="${DEPLOY_DIR}/files/opt/steam-tracker/run"

if [ ! -e "$HOSTS_FILE" ]; then
    echo "No hosts file found, you may need to rename ${HOSTS_FILE}.dist"
    exit
fi

read -p "Deploy fat jar? (y/n)[${DEPLOY_FAT_JAR}]: " DEPLOY_FAT_JAR

if [ ${DEPLOY_FAT_JAR} ] && [ ${DEPLOY_FAT_JAR} == "n" ]; then
    echo "Building and collecting dependencies..."
    gradle jar copyDependencies

    deps=($(find ${DEPLOY_DIR}/files/opt/steam-tracker/libs -type f -name '*.jar' -printf 'libs\\/%f\040'))

    template_file="${DEPLOY_DIR}/ansible/templates/run.sep.tmpl"
    libs=$(IFS=:; echo "${deps[*]}")
    sed -e "s/\%s/${libs}/g" ${template_file} > "${RUN_FILE}"
else
    echo "Building fat jar..."
    gradle bootRepackage

    template_file="${DEPLOY_DIR}/ansible/templates/run.fat.tmpl"
    cp ${template_file} ${RUN_FILE}
fi

chmod +x ${RUN_FILE}

echo "Deploying..."
ansible-playbook -i ${HOSTS_FILE} ${DEPLOY_DIR}/ansible/playbook.yml -u ${DEPLOY_USER} -s -K