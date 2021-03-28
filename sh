#!/bin/bash
git lfs pull

pwd
sudo apt install ffmpeg -y
du -sh out.mp4
file out.mp4
ffmpeg -i ./out.mp4
ffmpeg -i ./out.mp4 -filter "setpts=0.01*PTS" -q:v 0 out_2.mp4

