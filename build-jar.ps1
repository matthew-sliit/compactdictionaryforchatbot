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
    "generating dictionary Service"
    jar cfm output\dictionary.jar src\producer\worddict\dictionaryservice_manifest.mf -C classes \producer\worddict
    "generating dictionary English Client"
    jar cfm output\esdictionary.jar src\consumer\worddict\en\dictionaryuser_manifest.mf -C classes \consumer\worddict\en
    "generating dictionary Spanish Client"
    jar cfm output\endictionary.jar src\consumer\worddict\es\dictionaryuser_manifest.mf -C classes \consumer\worddict\es
    "generating translator Service"
    jar cfm output\translator.jar src\producer\translator\translatorservice_manifest.mf -C classes \producer\translator
    "generating translator Client"
    jar cfm output\translatoruser.jar src\consumer\translator\translatoruser_manifest.mf -C classes \consumer\translator
    "generating chatbot Service"
    jar cfm output\botsvc.jar src\producer\chatbot\manifest_chatbot.mf -C classes \producer\chatbot
    "generating chatbot Client"
    jar cfm output\botclient.jar src\consumer\chatbot\manifest_chatbotuser.mf -C classes \consumer\chatbot
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