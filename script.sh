# Stop, run, and clean
result=$(docker container ls -a -f name=rlstop-backend)
if [[ -n "$result" ]]; then
  docker stop rlstop-backend
  docker rm rlstop-backend
fi