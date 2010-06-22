<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: hover.xsl
Author: illbagna
Description: XSL stylesheet for the retreival of the encoded document's content.
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:rgb="http://rgb"
  exclude-result-prefixes="xsl xs rgb">
 


  <xsl:output method="text" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/>


  <xsl:param name="witNum"/>




  <!-- newline -->
  <xsl:variable name="newline">
     <br />
  </xsl:variable>
  <!-- blank -->
  <xsl:variable name="blank">
     <xsl:text> </xsl:text>
  </xsl:variable>
  <!-- comma -->
  <xsl:variable name="comma">
     <xsl:text>, </xsl:text>
  </xsl:variable>
  <!-- minus -->
  <xsl:variable name="minus">
     <xsl:text>-</xsl:text>
  </xsl:variable>
  <!-- separator -->
  <xsl:variable name="separator">
     <xsl:text>|</xsl:text>
  </xsl:variable>




  <xsl:template match="/">
	<xsl:text>content</xsl:text>
	<xsl:copy-of select="$separator"/>
	<!-- does the given document has its title in the "head" element ? -->
	<xsl:variable name="has-title-in-head" select="rgb:has-element(//*:head[not(ancestor::*:note)])"/> <!-- create a boolean flag for this check -->
	<xsl:variable name="content_title" as="element()*"> <!-- create a variable element for title's location -->
	  <xsl:choose>
		<xsl:when test="boolean($has-title-in-head)">
		  <xsl:sequence select="//*:head"/>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:sequence select="//element()[@type and ((name() = 'title') or (name() = 'head'))]"/>
		</xsl:otherwise>
	  </xsl:choose>
	</xsl:variable>

	<h3><xsl:apply-templates select="$content_title"/></h3>
	<xsl:copy-of select="$blank"/>



	<!-- boolean flag for poem / prose -->
	<xsl:variable name="is-poem" select="rgb:has-element(//*:lg[not(ancestor::*:note)])"/>
	<!-- variable element for the body's location -->
	<xsl:variable name="content_body" as="element()*">
	  <xsl:if test="boolean($is-poem)">
		<xsl:sequence select="//element()[
		  contains(name(), 'app')
		  and ((child::*:p) or (child::*:app) or (child::*:rdg))
		  and not(child::*:witList)
		  and not(ancestor::*:lg)
		  and not(ancestor::node() = $content_title)]"/>
		<!--and not(child::node()[contains(name(), $content_title)]) ]"/-->
		<!--
		<xsl:sequence select="//element()[
		  contains(name(), 'div')
		  and ((child::*:p) or (child::*:app))
		  and not(child::*:witList)
		  and not(child::*:lg)]"/>
		  -->
		<!-- <xsl:sequence select="//element()[contains(name(), 'div') and not(child::*:witList) and not(child::*:lg) and not(child::node()[contains(name(), $content_title)])]"/> -->
		<xsl:sequence select="//*:lg[not(ancestor::*:note) and not(ancestor::*:teiHeader)]"/>
		<!-- bisogna ancora includere nella sequenza tutti gli elementi app che sono al di fuori di "lg" -->
	  </xsl:if>
	</xsl:variable>
	
	<xsl:apply-templates select="$content_body"/>
	<!--xsl:apply-templates select="//*:lg[not(ancestor::*:note)]"/-->
	<!--xsl:apply-templates select="//*:app[not(ancestor::*:note) and not(ancestor::*:lg) and not(ancestor::*:)]"/-->
  </xsl:template>




  <!-- lb -->
  <xsl:template match="*:lb[not(ancestor::*:note)]">
	<xsl:copy-of select="$newline"/>
  </xsl:template>




  <!-- p -->
  <xsl:template match="*:p[not(ancestor::*:note)]">
	<p>
	  <xsl:apply-templates/>
	</p>
  </xsl:template>




  <!-- lg | l -->
  <xsl:template match="*:lg[not(ancestor::*:note)] | *:l[not(ancestor::*:note)]">
	<xsl:apply-templates/>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="$newline"/>
	</xsl:if>
  </xsl:template>




  <!-- app -->
  <xsl:template match="*:app[not(ancestor::*:note)]">
	<xsl:apply-templates/>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="$blank"/>
	</xsl:if>
  </xsl:template>




  <!-- rdg -->
  <xsl:template match="*:rdg">
	<xsl:if test="contains(@wit, $witNum)">
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	  <xsl:apply-templates/>
	</xsl:if>
  </xsl:template>



  <!-- hi -->
  <xsl:template match="*:hi">
	<u><xsl:apply-templates/></u>
  </xsl:template>



  <!-- del -->
  <xsl:template match="*:del">
	<del><xsl:apply-templates/></del>
  </xsl:template>




  <!-- add -->
  <xsl:template match="*:add">
	<em><xsl:apply-templates/></em>
  </xsl:template>




  <!-- note -->
  <xsl:template match="*:note"/>




  <!-- function that verifies the presence of an element() in the given document -->
  <xsl:function name="rgb:has-element" as="xs:boolean">
	<xsl:param name="element" as="element()?"/> <!-- there may be no element()s -->

	<xsl:choose>
	  <!-- in case the sequence contains at least an element() -->
	  <xsl:when test="string-length(string($element)) &gt; 0">
		<xsl:sequence select="true()"/>
	  </xsl:when>

	  <!-- in case of an empty sequence -->
	  <xsl:otherwise>
		<xsl:sequence select="false()"/>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:function>

</xsl:stylesheet>
