#!/bin/sh
#
# (C) Copyright 2016 HP Development Company, L.P.
# Confidential computer software. Valid license from HP required for possession, use or copying.
# Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
# Computer Software Documentation, and Technical Data for Commercial Items are licensed
# to the U.S. Government under vendor's standard commercial license.
#

# Show links in this container for facilitating debug
echo "Hosts file:"
cat /etc/hosts

# Banner
echo "  ___   _ _____ ___ _____ ___ __  __ ___ "
echo " |   \ /_|_   _| __|_   _|_ _|  \/  | __|"
echo " | |) / _ \| | | _|  | |  | || |\/| | _| "
echo " |___/_/ \_|_| |___| |_| |___|_|  |_|___|"
echo " "
echo "Daveeeeeeeeee!!!!";
echo "email: galo@hp.com";


# Start Service
echo "=> Starting service.."

# Start Service
java  -Xnoagent -Djsse.enableSNIExtension=false -Dserver.port=80 -Djava.security.egd=file:/dev/./urandom -jar /app/ccp-crs-datetime.jar
