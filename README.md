# txt2selenium

A simple tool to run headless html unit tests that are described in simple text-files using selenium.

## Actions

### Assert Checked

```assertChecked true|false```

Checks if a previous selected elements status is the same as true|false.

### Assert Equals

```assertEquals expectedIdentifier actualIdentifier```
```assertEquals true|false expectedIdentiier actualIdentifier```

Checks if the previous read variable identified by actualIdentifier is equal to the element contained in the Compare Strings.
If the true|false flag is set to false, this check can be used to check that the elements do not match.

### Click

```click```

This action will Click on an previous selected element.

### Method

```method methodName```

Will run a given Method that can contain multiple Actions.

### Open

```open URL```

Will open the given URL. The Action can also be used to open a local file.

```read varName```

Will read the previous selected elements content and safe it in the readVars under the given name 'varName'.
If the action is used multiple times on the same varName, the content will be overwritten.

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

After selecting an element where you can type text to (e.g. a input-field) you can use this action to fill the field with content.