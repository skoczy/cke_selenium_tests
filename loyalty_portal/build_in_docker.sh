#!/bin/sh

#######
#
# Uses the given docker image to start a container and run the given command
#
# Usage: ./build_in_docker.sh <image> <bash command> <docker env> <docker argv>
# Example: ./build_in_docker.sh sfr:engage_dev "(mongod --smallfiles > mongodb.log &) ; (sleep 40) ; ( gradle integrationTest )" "URL=yo" "--dns=172.17.42.1"
#
#######

docker_image=$1
bash_command=$2
docker_env=$3
docker_args=$4
timestamp=`date +%s`
here=`pwd`
gradleCacheDir="/var/lib/jenkins/gradleCaches/${JOB_NAME}"
username=`whoami`
localNodeModulesDir="src/ui/node_modules"

echo "Building in docker image $docker_image"
echo "Running build command $bash_command"
echo "Timestamp is $timestamp"
echo "I am in $here"
echo "and my username is $username"

# build the image if it does not exist in cache
sudo docker build -t ${docker_image} -f Dockerfile.Build .

# backup local node_modules
if [ -d "$localNodeModulesDir" ]; then
    echo "Backing up $localNodeModulesDir"
    mv "$localNodeModulesDir" src/ui/.node_modules
fi

# run build command in new container
sudo docker run -t -a stdout -a stderr -e="${docker_env}" ${docker_args} --rm --volume ${here}:/workspace:rw --volume ${gradleCacheDir}:/root/.gradle:rw ${docker_image} bash -c "ln -s /npm/node_modules /workspace/src/ui/node_modules; ${bash_command}"

# commented out for my local vagrant box running docker server
# sudo docker run -t -a stdout -a stderr --rm --volume /vagrant:/workspace:rw sfr:sitemaster_build bash -c "${bash_command}"


# change owner back to jenkins so it can do stuff with the files after docker exits
sudo chown -R jenkins .
sudo chgrp -R jenkins .
# remove symlink
if [ -L "$localNodeModulesDir" ] ; then
    echo "Removing symlink $localNodeModulesDir"
    rm "$localNodeModulesDir"
fi
# restore local node_modules
if [ -d src/ui/.node_modules ]; then
    echo "Restoring $localNodeModulesDir"
    mv src/ui/.node_modules "$localNodeModulesDir"
fi