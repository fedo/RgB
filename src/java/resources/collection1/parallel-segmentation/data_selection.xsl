<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : data_selection.xsl
    Created on : April 30, 2010, 12:36 PM
    Author     : illbagna
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="//witList/witness[@sigil]">
        <html>
            <head>
                <title>data_selection.xsl</title>
            </head>
            <body>
                <p>ciccia</p>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
