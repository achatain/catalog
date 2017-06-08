echo ''
echo '************************'
echo 'Building the frontend...'
echo '************************'
echo ''

cd ui

echo '1. Building code ...'
npm run build

echo '2. Moving build to webapp folder ...'
cp -prv build/* ../src/main/webapp

echo ''
echo '****************************'
echo 'Frontend built successfully!'
echo '****************************'
echo ''

#echo '************************'
#echo 'Building the backend...'
#echo '************************'
#mvn clean package -DskipTests -DskipITs -T 1.5C