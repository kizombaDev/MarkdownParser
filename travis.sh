#!/bin/bash
set -euo pipefail

# Minimal Maven settings
export MAVEN_OPTS="-Xmx1G -Xms128m"
MAVEN_ARGS="-Dmaven.test.redirectTestOutputToFile=false -Dsurefire.useFile=false -B -e -V "

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  echo 'Build and analyze master'
  # Fetch all commit history so that SonarQube has exact blame information
  # for issue auto-assignment
  # This command can fail with "fatal: --unshallow on a complete repository does not make sense"
  # if there are not enough commits in the Git repository (even if Travis executed git clone --depth 50).
  # For this reason errors are ignored with "|| true"
  git fetch --unshallow || true

  mvn org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar \
        $MAVEN_ARGS \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.login=$SONAR_TOKEN \
        -Dsonar.organization=kizombadev-github

elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then

  echo 'Build and analyze internal pull request'
  mvn org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar \
      $MAVEN_ARGS \
      -Dsource.skip=true \
      -Dsonar.analysis.mode=preview \
      -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
      -Dsonar.github.repository=$TRAVIS_REPO_SLUG \
      -Dsonar.github.oauth=$GITHUB_TOKEN \
      -Dsonar.host.url=$SONAR_HOST_URL \
      -Dsonar.login=$SONAR_TOKEN \
      -Dsonar.organization=kizombadev-github
else
  echo 'Build feature branch or external pull request'
  mvn install $MAVEN_ARGS -Dsource.skip=true
fi