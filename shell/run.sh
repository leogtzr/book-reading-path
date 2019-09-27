#!/bin/bash
set -o xtrace
set -o nounset
set -o pipefail

readonly work_dir="$(dirname "$(readlink --canonicalize-existing "${0}")")"
readonly books_file="${work_dir}/books"
readonly error_missing_books_file=80

if [[ ! -f "${books_file}" ]]; then
    echo "books file missing" >&2
    exit ${error_missing_books_file}
fi

selected_book=" "

while [[ ! -z "${selected_book}" ]]; do
    selected_book=$(grep --extended-regexp --invert-match ',x$' "${books_file}" | dmenu -b -l 50 -nb "#100" -nf "#b9c0af" -sb "#000" -sf "#afff2f" -i)
    if [[ ! -z "${selected_book}" ]]; then
        number_line=$(grep --line-number --fixed-strings "${selected_book}" "${books_file}" | awk --field-separator ':' '{print $1}')
        if [[ ! -z "${number_line}" ]]; then
            sed --in-place "${number_line}s/\(.*\)/\1,x/g" "${books_file}"
        fi
    fi
done

exit 0
