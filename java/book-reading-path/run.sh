#!/bin/bash
set -o xtrace
set -o nounset
set -o pipefail

readonly work_dir="$(dirname "$(readlink --canonicalize-existing "${0}")")"
readonly target_dir="${work_dir}/target"
readonly error_missing_jar_file=80

get_jar_file() {
    ls -1 "${target_dir}" | grep --extended-regexp "jar$"
}

jar_file=$(get_jar_file)
if [[ -z "${jar_file}" ]]; then
    echo "no jar file found in ${target_dir}" >&2
    exit ${error_missing_jar_file}
fi

jar_file="${target_dir}/${jar_file}"

java -jar "${jar_file}"

exit 0
