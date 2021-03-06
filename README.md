## Getting Started

This application is useful for encrypting and decrypting Strings and .yaml files using SecureProperties.jar 

## Build

To build this project secure properties.jar is required.  
Either specify it in `%CLASSPATH%` env variable or use cmdline arg. 

```
javac -d . Toast.java  
javac -d . -cp .\secure-properties-tool.jar;. Gui.java  
```

## Run

To run this download the executable from releases [here](https://github.com/bobby753/Secure-Props-GUI/releases/)  

<p align="center">
    <strong>OR</strong>
</p>


```
java -cp .\secure-properties-tool.jar;. gui/Gui 
```

## Additional Functionalities

* Copy to clipboard when clicked on output box.
* Text gets selected automatically when clicked on input box.

## Credits

- GUI  generated using `GuiGenie`  
- Toast Notification [Stackoverflow](https://stackoverflow.com/a/24716231)  
- Git Ignore [Gist File](https://gist.github.com/dedunumax/54e82214715e35439227) 

