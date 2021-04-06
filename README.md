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
Scenario 03: Sentence Checker  
  Producer: ?? + Word Dictionary  
  Consumer: Checking Sentences  
</pre>

### Task List:
- [x] WordDictionary
   - [ ] English Dictionary
   - [ ] Sinhala Dictionary
   - [x] Dictionary Exception handling
   - [x] Interaction Terminal
- [ ] Sentence Translator
   - [ ] English to Sinhala 
- [ ] Word Translator
   - [ ] English to Sinhala
   - [x] Interaction Terminal
- [ ] English Language Sentence Checker 
- [ ] Add Apache Felix framework
- [ ] Add OSGi Activator classes
- [ ] Add Manifest files
- [ ] Generate jar files

|Allocation|contributor|
|-|-|
|English Dictionary|[Amanda](https://github.com/amandaaaim)|
|Sinhala Dictionary|[Deshani](https://github.com/DeshaniMAWD)|
|Translator|[Vidura](https://github.com/vidurasathsara99)|
|Sentence Checker|Me|

Initial Project Architecture:
```bash
├───bin
│       gson-2.8.6.jar
└───src
    ├───sentencechecker
    ├───testclasses
    │   ├───translators
    │   │       SpanishWordToFrenchWordTest.java
    │   │
    │   └───worddict
    │           DictionaryTest.java
    │
    ├───translator
    │   │   SpanishWordToFrenchWord.java
    │   │
    │   └───service
    │           SentenceTranslator.java
    │           WordTranslator.java
    │
    └───worddict
        │   FrenchDictionary.java
        │   SpanishDictionary.java
        │
        ├───commons
        │       DictionaryException.java
        │       WordData.java
        │
        └───service
                WordDictionary.java
```
