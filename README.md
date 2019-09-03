# IntelliMind
Android App demonstrating the usage of Google Speech to Text API<br><br>

<p float="left">
<img  src="https://github.com/pranavj7Z/IntelliMind/blob/master/screenshot1.png" height="350" alt="Screenshot"/>
<img src="https://github.com/pranavj7Z/IntelliMind/blob/master/screenshot2.png" height="350" alt="Screenshot"/>
<img  src="https://github.com/pranavj7Z/IntelliMind/blob/master/screenshot4.png" height="350" alt="Screenshot"/>
<img  src="https://github.com/pranavj7Z/IntelliMind/blob/master/screenshot5.png" height="350" alt="Screenshot"/>
</p><br><br>

# Features
•  Converts Speech to Text using the Google Speech to Text API.<br><br>
•  Stop listening | Start listening modes are controlled with a custom animated Mic button<br><br>
•  Pressing Mic starts listening, Pressing Mic again deactivates it.<br><br>
•  Automatically detect user Locale and set it as the default language code for accurate English voice recognition<br><br>
•  Auto detects compeletion and starts searching after 3 seconds of silence.<br><br>
•  Displays the spoken text in a Custom autocomplete search box<br><br>
•  Allow user to edit the spoken text<br><br>
•  Stores user query into SQLite database for future reference<br><br>
•  Displays a list of suggestions from the Database using the TRIE DataStructure based on previous search queries<br><br>
•  Displays 5 recent search queries using an ArrayList by storing it in SharedPreferences<br><br>
•  The voice commmand <b>"your query" SEARCH</b> activates search.<br><br>
•  The voice commmand <b>"your query" STOP</b> erases the search text and disables the voice listener.<br><br>
•  User can set the prefered language code as per their english accent for better recognition from the settings.<br><br>
•  User can enable and disable the voice commands like SEARCH and STOP from the settings.<br><br>



