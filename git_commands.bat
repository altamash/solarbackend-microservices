echo off
echo -----------------------------------------
echo *** Deploy till preprod ***
echo -----------------------------------------
set current_branch=
for /F "delims=" %%n in ('git branch --show-current') do set "current_branch=%%n"
if "%current_branch%"=="" echo Not a git branch! && goto :EOF
if not "%current_branch%"=="development" (echo 'Not on development branch' & exit)
echo Starting
set commit_message=%1
if "%commit_message%"=="" (set commit_message=Update)
echo ******* git status
call git status
echo ******* git pull origin development
call git pull origin development
echo ******* git commit -am %commit_message%
call git commit -am %commit_message%
echo ******* git push origin development
call git push origin development
echo ******* git checkout stage
call git checkout stage
echo ******* git pull origin stage
call git pull origin stage
echo ******* git pull origin development
call git pull origin development
echo *******  git push origin stage
call git push origin stage
echo ******* git checkout preprod
call git checkout preprod
echo ******* git pull origin preprod
call git pull origin preprod
echo ******* git pull origin stage
call git pull origin stage
echo ******* git push origin preprod
call git push origin preprod
echo ******* git checkout development
call git checkout development
echo All done