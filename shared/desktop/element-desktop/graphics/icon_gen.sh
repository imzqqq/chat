#!/bin/bash

mydir="$(dirname "$(realpath "$0")")"

base_out="$mydir/../res/img"

export_rect() {
    w="$1"
    h="$2"
    in="$3"
    out="$4"
    inkscape -w "$w" -h "$h" --export-filename="$out" -C "$in"
}
export_square() {
    size="$1"
    in="$2"
    out="$3"
    export_rect "$1" "$size" "$in" "$out"
}

export_square 256 "$mydir/ic_launcher_sc.svg" "$base_out/element.png"

magick convert "$base_out/element.png" "$base_out/element.ico"

for f in "$base_out"/*; do
    pngcrush -ow "$f"
done




base_out="$mydir/../build"

for i in 16 24 48 64 96 128 256 512 1024; do
    export_square "$i" "$mydir/ic_launcher_sc.svg" "$base_out/icons/$i"x"$i.png"
done

export_square "320" "$mydir/ic_launcher_sc.svg" "$base_out/install-spinner.png"
magick convert "$base_out/install-spinner.png" "$base_out/install-spinner.gif"
rm  "$base_out/install-spinner.png"

magick convert "$base_out/icons/256x256.png" "$base_out/icon.ico"
magick convert "$base_out/icons/1024x1024.png" "$base_out/icon.icns"
rm "$base_out/icons/1024x1024.png"

for f in "$base_out"/*.png; do
    pngcrush -ow "$f"
done
