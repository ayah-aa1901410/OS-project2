#!/bin/bash
shopt -s extglob

fsdir=~/Documents/FileSystem
cd $fsdir

echo loggingbash

now=$(date +'%d-%m-%Y')

for dir in *; do
  due_date=`head -n 1 $fsdir/$dir/InstructorFiles/*`
echo $due_date
echo $now

if [[ "$now" -ge "$due_date" ]]; then

  for teamdir in $dir/!(InstructorFiles); do
    course=$(ls $fsdir/$teamdir)

    projectdir="$fsdir/$teamdir/$course"
    echo "$projectdir"
    attempts=0
    for FeedbackFile in $projectdir/FeedbackFolder/*; do

      ((++attempts))

      content=$(cat $FeedbackFile)
      echo $content >> $projectdir/FeedbackFolder/Logging.txt

    done
    tag=$( tail -n 1 $projectdir/FeedbackFolder/Logging.txt )

    if [[ "$tag" == *"Right"* ]]; then
      score=100
    else
      score=0
    fi

    echo "Final Score: $score" >> $projectdir/FeedbackFolder/Logging.txt
  #if Right score=100, Wrong score=0
  #Adding date's story
    echo "Total No. of attempts: $attempts" >> $projectdir/FeedbackFolder/Logging.txt
  done
else
  echo false
fi
done






#------------

# due_date=$(head -n 1 ~/Documents/FileSystem/CMPS151_SalehAlhazbi/InstructorFiles/CMPS151_project1_solution)
#echo $now
#echo $due_date
#if [[ "$now" -ge "$due_date" ]]; then
# echo greater
#cat FeedbackFile*.$directoryName >Logging.$directoryName
#fi
