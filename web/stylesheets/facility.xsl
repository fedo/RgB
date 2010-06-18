<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: facility.xsl
Author: illbagna
Description: XSL stylesheet for the data retrieval associated to any document witness
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema">


  <xsl:output method="text" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/>



  <xsl:template match="/">
	<xsl:apply-templates select="//*:witList//*:witness"/>
  </xsl:template>



  <!-- proprieta` di un witness -->
  <xsl:template match="*:witList//*:witness">
	<xsl:choose>
	  <xsl:when test="./@der">
		<xsl:variable name="w" as="xs:string*">
		  <xsl:sequence select="(@der, @missing, @sigil, @id)"/>
		</xsl:variable>
		<xsl:value-of select="$w" separator=" "/>
	  </xsl:when>
	  <xsl:otherwise>
		<xsl:variable name="w" as="xs:string*">
		  <xsl:sequence select="('null', @missing, @sigil, @id)"/>
		</xsl:variable>
		<xsl:value-of select="$w" separator=" "/>
	  </xsl:otherwise>
	</xsl:choose>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="'-'"/>
	</xsl:if>
  </xsl:template>



  <xsl:template match="text()">
	<xsl:text/>
  </xsl:template>

</xsl:stylesheet>
