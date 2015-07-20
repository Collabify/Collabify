#!/bin/sh
convert $1 -resize 48x48 -colorspace RGB drawable-mdpi/$1
convert $1 -resize 72x72 -colorspace RGB drawable-hdpi/$1
convert $1 -resize 96x96 -colorspace RGB drawable-xhdpi/$1
convert $1 -resize 144x144 -colorspace RGB drawable-xxhdpi/$1
mv $1 drawable-xxxhdpi/$1
