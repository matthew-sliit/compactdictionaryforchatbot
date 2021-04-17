# PortableDictionaryOSGi
Group project for Software Architecture Assignment 01   
Topic: **PortableDictionary With OSGi**
<pre>
Scenario 01: Dictionary usage  
  Producer: Word Dictionary  
  Consumer: Addnew, edit, getall, remove  
Scenario 02: Translation  
  Producer: Translator + Word Dictionary  
  Consumer: Translation, mapping words  
Scenario 03 (Changed): Simple ChatBot  
  Producer: ChatBotService + Word Dictionary  
  Consumer: ChatBotUser  
</pre>
#### External Libraries 
<pre>
  Dependencies: gson-2.8.6.jar, felix-7.0.0.jar
</pre>
### Task List:
- [x] WordDictionary
   - [x] English Dictionary (Amanda)
   - [x] Sinhala Dictionary (Deshani)
   - [x] Dictionary Exception handling
   - [x] Interaction Terminal for Dictionary inputs
      - [x] English Dictionary handling (Amanda)
      - [x] Sinhala Dictionary handling (Deshani)
   - [x] Save words as preferences using gson
- [x] Sentence Translator
   - [x] English to Sinhala concept
   - [x] English Sentence to Sinhala Sentence
   - [x] Save translated sentences as preferences using gson
   - [x] Run Experiments 
- [x] Word Translator
   - [x] English to Sinhala
   - [x] Interaction Terminal
   - [x] Save translated words as preferences using gson
- [ ] English Language Sentence Checker:Failed DeadEND
- [x] Simple ChatBot
   - [x] Predefine Words and Structures   
   - [x] Save user preferences  
   - [x] Generate Meaningful reply for given input  
   - [x] Multithread  
   - [x] Use Dictionary services  
   - [ ] Experiment, Identify Strengths and Weaknesses 
- [x] Add Apache Felix framework
- [x] Add OSGi Activator classes
- [x] Add Manifest files
- [x] Generate jar files
- [x] Create batch script to robocopy classes to {folder}
- [x] Create batch script to auto generate jar files
### Allocation:  
|Allocation|contributor|
|-|-|
|English Dictionary|[Amanda](https://github.com/amandaaaim)|
|Sinhala Dictionary|[Deshani](https://github.com/DeshaniMAWD)|
|Translator|[Vidura](https://github.com/vidurasathsara99)|
|Simple Chatbot|Me|

### Initial Project Architecture:  
```bash
├───bin
│       gson-2.8.6.jar
├───output
│   │       dictionary.jar
│   │       dictionaryuser.jar
│   │       translator.jar
│   │       translatoruser.jar
│   │
│   └───src
│       ├───consumer
│       │   ├───translator
│       │   │       TranslatorConsumer.java
│       │   │       translatoruser_manifest.mf
│       │   │
│       │   └───worddict
│       │           DictionaryConsumer.java
│       │           dictionaryuser_manifest.mf
│       │
│       ├───producer
│       │   ├───chatbot
│       │   │   │   BotMind.java
│       │   │   │   BotProvider.java
│       │   │   │   EnglishChatbot.java
│       │   │   │   EnglishSentenceStructure.java
│       │   │   │   manifest_chatbot.mf
│       │   │   │
│       │   │   ├───commons
│       │   │   │       ChatBotException.java
│       │   │   │
│       │   │   ├───runners
│       │   │   │       PerspectiveIdentifier.java
│       │   │   │       SentenceAnalyzer.java
│       │   │   │       SentenceWordTypeIdentifier.java
│       │   │   │
│       │   │   └───service
│       │   │           ChatbotMemory.java
│       │   │           ChatbotService.java
│       │   │
│       │   ├───translator
│       │   │   │   EnglishSentenceToSinhalaSentence.java
│       │   │   │   EnglishWordToSinhalaWord.java
│       │   │   │   SpanishWordToFrenchWord.java
│       │   │   │   TranslatorProvider.java
│       │   │   │   translatorservice_manifest.mf
│       │   │   │
│       │   │   ├───common
│       │   │   │       TranslatorException.java
│       │   │   │
│       │   │   └───service
│       │   │           SentenceTranslator.java
│       │   │           WordTranslator.java
│       │   │
│       │   └───worddict
│       │       │   DictionaryProvider.java
│       │       │   dictionaryservice_manifest.mf
│       │       │   EnglishDictionary.java
│       │       │   FrenchDictionary.java
│       │       │   GenericDictionary.java
│       │       │   SinhalaDictionary.java
│       │       │   SpanishDictionary.java
│       │       │
│       │       ├───commons
│       │       │       DictionaryException.java
│       │       │       WordData.java
│       │       │
│       │       └───service
│       │               WordDictionary.java
│       │
│       └───testclasses
│           ├───chatbot
│           │       ChatBotTest.java
│           │
│           ├───sentencing
│           │       SentenceCheckerTest.java
│           │
│           ├───testtranslators
│           │       SpanishWordToFrenchWordTest.java
│           │       TestEnglishSentenceToSinhalaSentence.java
│           │       TestEnglishWordToSinhala.java
│           │
│           └───worddict
│                   DictionaryTest.java
│                   EnglishDicTest.java
```
