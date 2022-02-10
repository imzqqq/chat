#!/bin/bash

mydir="$(dirname "$(realpath "$0")")"

base_out="$mydir/../res/vector-icons"

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

for i in 1024 120 150 152 180 24 300 44 48 50 76 88; do
    export_square "$i" "$mydir/ic_launcher_sc.svg" "$base_out/$i.png"
done

for i in 114 120 144 152 180 57 60 72 76; do
    export_square "$i" "$mydir/store_icon.svg" "$base_out/apple-touch-icon-$i.png"
done

for i in 150 310 70; do
    export_square "$i" "$mydir/store_icon.svg" "$base_out/mstile-$i.png"
done

export_rect 620 300 "$mydir/icon_wide_transparent.svg" "$base_out/620x300.png"
export_rect 1240 600 "$mydir/icon_wide_transparent.svg" "$base_out/1240x600.png"
export_rect 310 150 "$mydir/icon_wide.svg" "$base_out/mstile-310x150.png"

magick convert "$base_out/48.png" "$base_out/favicon.ico"
rm "$base_out/48.png" # this was only created for favicon.ico

for f in "$base_out"/*; do
    pngcrush -ow "$f"
done
