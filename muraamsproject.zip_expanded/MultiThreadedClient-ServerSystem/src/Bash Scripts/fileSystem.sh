#!/bin/bash
mkdir ~/Documents/FileSystem
input=~/Documents/InputFiles/Courses
while IFS= read -r line
do
  mkdir ~/Documents/FileSystem/$line
  mkdir ~/Documents/FileSystem/$line/InstructorFiles
  input2=~/Documents/InputFiles/Teams
  while IFS= read -r line2
  do
    coursename=${line:0:7}
    teamscoursename=${line2:0:7}
    if [[ $coursename == $teamscoursename ]]; then
      mkdir ~/Documents/FileSystem/$line/$line2
      input3=~/Documents/InputFiles/Projects
      while IFS= read -r line3; do
        projectcoure=${line3:0:7}
        if [[ $coursename == $projectcoure ]]; then
          mkdir ~/Documents/FileSystem/$line/$line2/$line3
          mkdir ~/Documents/FileSystem/$line/$line2/$line3/TestCases
          mkdir ~/Documents/FileSystem/$line/$line2/$line3/FeedbackFolder
          mkdir ~/Documents/FileSystem/$line/$line2/$line3/CompObject
        fi
      done < "$input3"
    fi
    done < "$input2"
done < "$input"
