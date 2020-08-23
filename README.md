# txt2selenium

A simple tool to run headless html unit tests that are described in simple text-files using selenium.

txt2Selenium lets one create UI Tests without having to write complex Code in any language. Every test will be placed
in a simple text-file written in an easy syntax.

## Getting Started
Download the txt2Selenium-jar. Starting is as easy as
 
```java -jar txt2Selenium.jar PATH```

Where `PATH` is the path where txt2Selenium will search for test files, put screenshot-files and write logfiles to.
Inside of `PATH` one needs the directory `tests` where txt2Selenium will search for the test-files.

txt2Selenium identifies test-files by means of the file-extension `.t2s`.

## Test Structures
Inside of ones `PATH` one can create test-scenarios - each subdirectory describes one scenario where each `*.t2s` file
describes one test. Each scenario can contain multiple tests' ans sub-scenarios.

The names for the subdirectories are more or less freely selectable. There are just two saved names:

The filename `compareStrings.t2s` is reserved for containing strings to use in your tests (e.g. to compare values one
read from the page).

And the directory `methods` which can contain methods to use in ones tests.

A test could look something like this:

compareStrings.t2s
```
loginErrorMessage Invalid password
```
login.t2s
```
comment Will test the Error Message for the login
open http://localhost/typedcode-cms/admin
select id passwordField
type wrong password
sendspecialkey enter
select id errorMessage
read displayedErrorMessage
assertEquals loginErrorMessage displayedErrorMessage
```
This test will open the admin page on the localhost, search for the password field, type in the password (which in this
case will be the wrong password) and submit the form. Afterwards the error message is read and compared with `loginErrorMessage`
in `compareStrings.t2x`.

Every Action described in Actions can be used to create test-files.

### Methods
Methods can be used to describe often needed operations. Inside each scenario there can be a folder named `methods`. The
folder will be no additional subscenario. Instead, each 'test-file' within the methods folder can be used inside tests.
E.g.

Folders
```
tests
 - subScenario
   - methods
     - login.t2s
     - logout.t2s
   - changeSettings.t2s
```
In this case `changeSettings.t2s` in the `subScenario` could use the `login` and `logout` methods and use them in the test.
See the Action `method` for details on how to use methods.

### Compare Strings
The reserved file `compareStrings.t2s` can be used to describe texts to match against texts that are read inside a test.
The files contain simple key-value pairs which one key-value pair in each line.

compareStrings.ts
```
loginErrorMessage Wrong password
pageTitle MyPage Title
```
In this case there will be two texts available to compare content to. CompareStrings are additional. Which means that if
one creates a separate `compareStrings.t2s` file in ones sub-scenario then both CompareStrings will be available in that
scenario.

CompareStrings in a sub-scenario which have the save key as a string in the parent scenario will override the
value for this scenario and every subscenario.

## Actions

### Assert Checked

```assertChecked true|false```

Checks if a previous selected elements status is the same as true|false.

### Assert Equals

```assertEquals expectedIdentifier actualIdentifier```

```assertEquals true|false expectedIdentiier actualIdentifier```

Checks if the previous read variable identified by actualIdentifier is equal to the element contained in the Compare Strings.
If the true|false flag is set to false, this check can be used to check that the elements do not match.

### Assert Read Equals

```assertReadEquals expectedIdentifier actualIdentifier```

```assertReadEquals true|false expectedIdentiier actualIdentifier```

Works like ```assertEquals``` but compares two values that where previously read to each other.

### Click

```click```

This action will Click on an previous selected element.

### Comment

```comment MESSAGE```

Adds the given Message to the log.

### Method

```method methodName```

Will run a given Method that can contain multiple Actions.

### Open

```open URL```

Will open the given URL. The Action can also be used to open a local file.

```read varName```

Will read the previous selected elements content and safe it in the readVars under the given name 'varName'.
If the action is used multiple times on the same varName, the content will be overwritten.

### Send Special Key
```sendSpecialKey KEY```

Will send a special key (e.g. enter, tab...) to a previous selected element. Every key specified in 
https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/Keys.html is allowed.

### Screenshot

```screenshot```

```screenshot prefix```

Will save the currently open Page to the file-system. The name will be named like
```screenshot_DATE.html```

If the prefix is set, the resulting filename will be
```screenshot_prefix_DATE.html```

### Select

```select id idToSelect```

```select name nameToSelect```

```select xPath pathToElement```

This action will select the element identified by either the id- or name-Attribute or by the xPath.

### Type

```type text to type```

After selecting an element where one can type text to (e.g. a input-field) one can use this action to fill the field with content.