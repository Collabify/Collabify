#!/bin/sh
convert $1 -resize 65x65 drawable-mdpi/$1
convert $1 -resize 98x98 drawable-hdpi/$1
convert $1 -resize 130x130 drawable-xhdpi/$1
convert $1 -resize 195x195 drawable-xxhdpi/$1
mv $1 drawable-xxxhdpi/$1
