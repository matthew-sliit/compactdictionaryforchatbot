$projectDir = 'F:\eclipse-workspace\Sliit\PortableDictionaryOSGi'
$classesDir = 'F:\eclipse-workspace\Sliit\PortableDictionaryOSGi\classes'
Set-Location $projectDir
if( Test-Path -path "$classesDir" -pathtype container ){
  if(-not (Test-Path -path "$projectDir\output" -pathtype container)){
    "output folder not in project directory, creating folder.."
    try{
      New-Item -path "$projectDir" -name "output" -ItemType "directory"
      "successfully created the output folder"
    }catch{
      "An error occured when creating the output folder"
    }
  }
  if( Test-Path -path "$projectDir\output" -pathtype container ) {
    if( test-path -path "$projectDir\output\producer" -pathtype container){
      Get-ChildItem -path "$projectDir\output" *.java -Recurse | foreach { Remove-Item -Path $_.FullName }
      "tutorial folder found in output folder, copying changes from \classes"
      ROBOCOPY "$classesDir" "$projectDir\output" /mir
    }else{
      "tutorial folder NOT found in output folder, copying all from \classes"
      ROBOCOPY "$classesDir" "$projectDir\output" /mir
    }
    javac -version 
    "generating worddict Producer"
    jar cfm output\dictionary.jar src\producer\worddict\dictionaryservice_manifest.mf -C output \producer\worddict
    "generating worddict Consumer"
    jar cfm output\dictionaryuser.jar src\consumer\worddict\dictionaryuser_manifest.mf -C output \consumer\worddict
    "generating translator Producer"
    jar cfm output\translator.jar src\producer\translator\translatorservice_manifest.mf -C output \producer\translator
    "generating translator Consumer"
    jar cfm output\translatoruser.jar src\consumer\translator\translatoruser_manifest.mf -C output \consumer\translator
    
    #jar tf output\dictionary.jar

    #jar tf output\dictionaryuser.jar

    #install File:F:/eclipse-workspace/Sliit/PortableDictionaryOSGi/output/dictionary.jar

    #install File:F:/eclipse-workspace/Sliit/PortableDictionaryOSGi/output/dictionaryuser.jar

    #install File:F:/eclipse-workspace/Sliit/PortableDictionaryOSGi/bin/gson.jar
  }else{
  "output folder does not exist in project directory"
  }
}else{
  "no classes to create jar"
}
Read-Host -Prompt "Press Enter to exit"