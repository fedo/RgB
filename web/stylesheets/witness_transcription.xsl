<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: hover.xsl
Author: illbagna
Description: XSL stylesheet for the retreival of the number of the (eventual) witnesses
associated to the document's content, and the number of its (eventual) transcriptions
-->


<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fn="http://www.w3.org/2006/xpath-function"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:saxon-java="java:java.lang.Math">


  <xsl:output method="text" indent="no" omit-xml-declaration="yes" encoding="UTF-8"/>


  <xsl:variable name="separator">
	<xsl:text>|</xsl:text>
  </xsl:variable>



  <xsl:template match="/">

	<!-- witnesses -->
	<xsl:variable name="witnesses" as="xs:string*">
	  <xsl:for-each select="//*:witList//*:witness">
		<xsl:sequence select="@sigil"/>
	  </xsl:for-each>
	</xsl:variable>
	<xsl:value-of select="$witnesses" separator="-"/>

	<xsl:copy-of select="$separator"/>

	<!-- transcriptions -->
	<xsl:variable name="transcriptions" as="xs:string*">
	  <xsl:choose>
		<xsl:when test="//*:choice">
		  <xsl:call-template name="tradition"/>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:sequence select="'diplomatica'"/>
		</xsl:otherwise>
	  </xsl:choose>
	</xsl:variable>
	<xsl:value-of select="$transcriptions" separator="-"/>

  </xsl:template>



  <xsl:template name="tradition">
	<!-- regularization element ==> critical apparatus -->
	<xsl:if test="//*:choice//*:orig">
	  <xsl:sequence select="'diplomatica'"/>
	</xsl:if>
	<xsl:if test="//*:choice//*:abbr and //*:choice//*:expan">
	  <xsl:sequence select="'interpretativa'"/>
	</xsl:if>
	<xsl:if test="//*:choice//*:reg">
	  <xsl:sequence select="'critica'"/>
	</xsl:if>
  </xsl:template>

</xsl:stylesheet>
