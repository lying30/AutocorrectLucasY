# Autocorrect Tool

This project is a command-line autocorrect tool that runs continuously in the terminal. It mimics the behavior of autocorrect features found on mobile devices. Given a word, it checks against a dictionary and suggests close matches if the word is misspelled.

## Features
- Checks typed words against a dictionary
- Suggests up to **5 closest words** using Levenshtein edit distance
- Uses an edit distance **threshold of 3**
- Sorted suggestions: first by distance, then alphabetically
- Continuous user input (type `"exit"` to quit)

## How It Works
- The dictionary is loaded from a `.txt` file in the `dictionaries/` folder.
- Each input word is compared to all dictionary entries using the Levenshtein distance algorithm (tabulation/Dynamic Programming).
- Words within the threshold are returned as suggestions.