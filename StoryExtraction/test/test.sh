#!/bin/bash

work_dir="../out/production/StroyExtraction/"

cd $work_dir

while read line
do
  query=$line
  java storyextraction.MainWindow $query -d
done
