<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : witList.xsl
    Author     : illbagna
    Description: collection of XSLT stylesheets
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>

  <xsl:variable name="blank">
     <xsl:text> </xsl:text>
  </xsl:variable>


  <xsl:template match="/">
	<xsl:apply-templates select="//*:witList"/>
  </xsl:template>

  <!-- testimoni -->
  <xsl:template match="*:witList">
	<xsl:for-each select=".//*:witness[@sigil and not(@missing = 'true')]">

	  <xsl:if test="position() &gt; 1">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	  <xsl:value-of select="@sigil"/>
	</xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
