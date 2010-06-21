<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: hover.xsl
Author: illbagna
Description: XSL stylesheet for the retreival of all the services and relative parameters
associated to the given catalog
-->


<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema">


  <xsl:output method="text" indent="no" omit-xml-declaration="yes" encoding="UTF-8"/>


  <xsl:variable name="separator">
	<xsl:text>|</xsl:text>
  </xsl:variable>



  <xsl:template match="/">

	<xsl:for-each select="//*:service">
	  <xsl:variable name="service" as="xs:string*">
		<xsl:sequence select="concat(//base, '/', ./URI)"/>
		<xsl:sequence select="./HTTPmethod"/>
		<xsl:sequence select=".//input[1]"/>
		<xsl:sequence select=".//output[1]"/>
		<xsl:sequence select=".//params/param[@name]/@name"/>
	  </xsl:variable>

	  <xsl:value-of select="$service" separator="@"/>

	  <xsl:if test="position() != last()">
		<xsl:copy-of select="$separator"/>
	  </xsl:if>
	</xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
