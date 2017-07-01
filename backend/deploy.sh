#!/bin/sh
ansible-playbook -e "role=backend" ../deploy/site.yml -i ../deploy/hosts -b -K