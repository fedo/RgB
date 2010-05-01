<HTML
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/HTML4/">
<!--
    Document   : data_selection.xsl
    Created on : April 30, 2010, 12:36 PM
    Author     : illbagna
    Description:
        Purpose of transformation follows.
-->
    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <body>
        <xsl:value-of select="//witList/witness[1]/@sigil" />
    </body>
</HTML>