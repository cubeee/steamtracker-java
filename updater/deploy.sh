#!/bin/sh
ansible-playbook -e "role=updater" ../deploy/site.yml -i ../deploy/hosts -b -K