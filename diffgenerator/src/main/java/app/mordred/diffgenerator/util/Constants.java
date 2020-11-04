package app.mordred.diffgenerator.util;

public class Constants {

    public static final String TAG = "DiffGenerator";

    public static final int EXIT_CODE_ERROR = 1;

    public static final int EXIT_CODE_OK = 0;

    public static final String NEWLINE = System.lineSeparator();

    public static final String SYSOUT_MSG_DIRECTORIES_IDENTICAL = NEWLINE + "Directories are identical!";

    public static final String SYSOUT_MSG_FILES_IDENTICAL = NEWLINE + "Files are identical!";

    public static final String SYSOUT_MSG_DIRECTORIES_DIFFER = NEWLINE + "Directories differ!";

    public static final String SYSOUT_MSG_FILES_DIFFER = NEWLINE + "Files differ!";

    public static final int BYTE_BUFFER_SIZE_DETECT_BINARY = 16000;

    public static final String rawCssStyle = "body {\n" +
            "    text-align: center;\n" +
            "}\n" +
            "#wrapper {\n" +
            "    display: inline-block;\n" +
            "    margin-top: 1em;\n" +
            "    min-width: 800px;\n" +
            "}\n" +
            "#wrapper2 {\n" +
            "    font-family: sans-serif;\n" +
            "    text-align: left;\n" +
            "    display: inline-block;\n" +
            "    margin-top: 1em;\n" +
            "    min-width: 800px;\n" +
            "}\n" +
            "h1 {\n" +
            "    font-family: sans-serif;    \n" +
            "    color: #505050;\n" +
            "}\n" +
            "h2 {\n" +
            "    font-family: sans-serif;    \n" +
            "    color: #5c5c5c;\n" +
            "}\n" +
            "table th {\n" +
            "    border: 1px solid #d8d8d8;\n" +
            "    background: #fafafa;\n" +
            "    background: -moz-linear-gradient(#fafafa, #eaeaea);\n" +
            "    background: -webkit-linear-gradient(#fafafa, #eaeaea);\n" +
            "    -ms-filter: \"progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa',endColorstr='#eaeaea')\";\n" +
            "    color: #555;\n" +
            "    font: 14px sans-serif;\n" +
            "    overflow: hidden;\n" +
            "    padding: 16px 6px;\n" +
            "    text-shadow: 0 1px 0 white;\n" +
            "}\n" +
            "table tr {\n" +
            "    font-family: \"Lucida Console\", \"Courier New\", \"Courier\", monospace;\n" +
            "    font-size: 12px;\n" +
            "    line-height: 1.5em;\n" +
            "}\n" +
            "table {\n" +
            "    border: 1px solid #d8d8d8;\n" +
            "    margin-bottom: 1em;\n" +
            "    margin: 0;\n" +
            "    overflow: auto;\n" +
            "    padding: 0;\n" +
            "    text-align: left;\n" +
            "}\n" +
            "table > div {\n" +
            "    width: 100%:\n" +
            "}\n" +
            "a {\n" +
            "    color: #000099;\n" +
            "    text-decoration: none;\n" +
            "}\n" +
            "\n" +
            "a:hover \n" +
            "{\n" +
            "     color:#0000cc; \n" +
            "     font-weight: bold;\n" +
            "     text-decoration:underline; \n" +
            "     cursor:pointer;  \n" +
            "}\n" +
            ".delete {\n" +
            "    background-color: #ffeef0;\n" +
            "}\n" +
            ".insert {\n" +
            "    background-color: #e5ffee;\n" +
            "}\n" +
            ".info {\n" +
            "    color: #808080;\n" +
            "}\n" +
            ".attention {\n" +
            "    color: #990000;\n" +
            "}\n" +
            ".line-number {\n" +
            "    background: #f5f5f0;\n" +
            "    border-right: 1px solid #ccccb3;\n" +
            "}\n" +
            ".one-line-header {\n" +
            "    display: inline;\n" +
            "}\n" +
            ".expand-all-link {\n" +
            "    font-family: monospace;\n" +
            "}\n" +
            ".deleteChar {\n" +
            "    background-color: #ffb8c0;\n" +
            "    text-decoration: none;\n" +
            "}\n" +
            ".insertChar {\n" +
            "    background-color: #a9f2c0;\n" +
            "    text-decoration: none;\n" +
            "}";

    public static final String rawJs = "function showElement(id) {\n" +
            "\tvar divElement = document.getElementById(id);\n" +
            "\tv = divElement.style.display;\n" +
            "\tdivElement.style.display = v == \"inline\" ? \"none\" : \"inline\"\n" +
            "}\n" +
            "\n" +
            "function showElements(elemIds) {\n" +
            "\tvar atLeastOneCollapsed = false;\n" +
            "\tfor(var i = 0; i < elemIds.length; i++) {\n" +
            "\t\tif(document.getElementById(elemIds[i]).style.display == \"none\") {\n" +
            "\t\t\tatLeastOneCollapsed = true;\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\tif(atLeastOneCollapsed == true) {\n" +
            "\t\tfor(var i = 0; i < elemIds.length; i++) {\n" +
            "\t\t\tdocument.getElementById(elemIds[i]).style.display = \"inline\";\n" +
            "\t\t}\n" +
            "\t} else {\n" +
            "\t\tfor(var i = 0; i < elemIds.length; i++) {\n" +
            "\t\t\tdocument.getElementById(elemIds[i]).style.display = \"none\";\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}";

}
