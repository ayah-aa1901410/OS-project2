#!/bin/bash
shopt -s extglob

echo here
fsdir=~/Documents/MultiThreadedClient-ServerSystem/src/FileSystem
cd $fsdir

for dir in *; do
    echo $dir
    value=`tail -n +2 $fsdir/$dir/InstructorFiles/*`
    echo $value
    for teamdir in $dir/!(InstructorFiles); do
        #echo $teamdir
        course=$(ls $fsdir/$teamdir)
        #echo $course
        projectdir="$fsdir/$teamdir/$course"
        number=0
        multiTestCase=false;
        numTCs=0

        for projFile in `ls -ltr $projectdir/*`; do
        if [ -f "$projFile" ]; then
          chmod ugo+x "$projFile"
        number=$((number+1))
        #echo $projectdir
        if [[ ${projFile: -3} == ".sh" ]]; then

          if [ "$(ls -A $projectdir/TestCases)" ]; then
            multiTestCase=true
            numTCs=$(ls -1 $projectdir/TestCases/| wc -l)

            values=()
            answers=()

            i=0
            for testCase in `ls $projectdir/TestCases/*`; do

              values+=(`tail -n $(($i+2)) $fsdir/$dir/InstructorFiles/*`)
              answers+=($(echo $(while IFS= read line; do echo "$line"; done <$testCase| $projFile 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)))

              i=$(($i+1))

            done
          else
            answer=$($projFile 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)
          fi

        elif [[ ${projFile: -2} == ".c" ]]; then

          comp_object=$(echo $projFile| cut -d'.' -f 1)
          comp_object=`echo $comp_object|rev| cut -d'/' -f1 | rev`

          comp_object=$projectdir/CompObject/$comp_object
          gcc -o $comp_object $projFile

          if [ "$(ls -A $projectdir/TestCases)" ]; then
            multiTestCase=true
            numTCs=$(ls -1 $projectdir/TestCases/| wc -l)

            values=()
            answers=()

            i=0
            for testCase in `ls $projectdir/TestCases/*`; do

              values+=(`tail -n $(($i+2)) $fsdir/$dir/InstructorFiles/*`)
              answers+=($(echo $(while IFS= read line; do echo "$line"; done <$testCase| $comp_object 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)))

              i=$(($i+1))
            done
          else

            answer=$($comp_object 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)
          fi
          # implement same code in bash for running from multiple test cases with C

        elif [[ ${projFile: -5} == ".java" ]]; then
          # implement code to run from multiple test cases with java project

          $(javac -d $projectdir/CompObject/ $projFile)

          comp_object=$(echo $projFile| cut -d'.' -f 1)

          comp_object=`echo $comp_object|rev| cut -d'/' -f1 | rev`


          if [ "$(ls -A $projectdir/TestCases)" ]; then
            multiTestCase=true
            numTCs=$(ls -1 $projectdir/TestCases/| wc -l)

            values=()
            answers=()

            i=0
            for testCase in `ls $projectdir/TestCases/*`; do

              values+=(`tail -n $(($i+2)) $fsdir/$dir/InstructorFiles/*`)
              answers+=($(echo $(while IFS= read line; do echo "$line"; done <$testCase| java -cp $projectdir/CompObject $comp_object 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)))

              i=$(($i+1))
            done
          else

            answer=$(java -cp $projectdir/CompObject $comp_object 2> $projectdir/FeedbackFolder/FeedbackFile$number.txt)
          fi

        else
          echo "Unsupported file format" >> $projectdir/FeedbackFolder/FeedbackFile$number.txt
        fi


        if [[ $multiTestCase == true ]]; then
          correct=true

          for (( i = 0; i < numTCs; i++ )); do
            if [[ ${answers[$i]} != ${values[$i]} ]]; then
              correct=false
            fi
          done

          if [[ $correct == true ]]; then

            echo "attempt $number: Right" >> $projectdir/FeedbackFolder/FeedbackFile$number.txt

          elif [[ $correct == false ]]; then

            echo "attempt $number: Wrong" >> $projectdir/FeedbackFolder/FeedbackFile$number.txt

          fi

        else
          if [[ $answer == $value ]]; then

            echo "attempt $number: Right" >> $projectdir/FeedbackFolder/FeedbackFile$number.txt

          elif [[ $answer != $value ]]; then

            echo "attempt $number: Wrong" >> $projectdir/FeedbackFolder/FeedbackFile$number.txt

          fi
        fi
      fi
                #echo end of dir one
    done
    done
    #value=`cat InstructorFiles/`
done


echo done