<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : xsl_collection.xsl
    Author     : illbagna
    Description: collection of XSLT stylesheets
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>

  <xsl:variable name="newline">
     <br />
  </xsl:variable>


  <xsl:variable name="blank">
     <xsl:text> </xsl:text>
  </xsl:variable>


  <xsl:variable name="comma">
     <xsl:text>, </xsl:text>
  </xsl:variable>


  <!-- titolo del documento -->
  <xsl:template name="titolo">
     <xsl:param name="radix"/>
     
     <b><xsl:text>Titolo:</xsl:text></b>
     <xsl:value-of select="$blank"/>
     <span id="docTitle">
        <xsl:value-of select="$radix//*:title"/>
     </span>
  </xsl:template>
  

  <!-- autore del documento -->
  <xsl:template name="autore">
     <xsl:param name="radix"/>

     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Autore:</xsl:text></b>

     <xsl:value-of select="$blank"/>
     <span id="docAuthor">
        <xsl:value-of select="$radix//*:author"/>
     </span>
  </xsl:template>


  <!-- testimoni -->
  <xsl:template match="*:witList">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Testimoni:</xsl:text></b>

      <xsl:copy-of select="$blank"/>
      <span id="docWit">
        <xsl:for-each select=".//*:witness[@sigil]">

           <xsl:if test="position() &gt; 1">
              <xsl:copy-of select="$comma"/>
           </xsl:if>
           <xsl:value-of select="@sigil"/>
        </xsl:for-each>
     </span>
  </xsl:template>


  <!-- revisori -->
  <xsl:template match="*:revisionDesc">
	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Revisioni:</xsl:text></b>

	<xsl:copy-of select="$blank"/>
	<span id="docRev">
	  <xsl:value-of select=".//*:date"/>

	  <xsl:if test=".//*:date">
		<xsl:copy-of select="$newline"/>
	  </xsl:if>
	  <xsl:value-of select=".//*:name"/>

	  <xsl:if test=".//*:date or .//*:name">
		<xsl:copy-of select="$newline"/>
	  </xsl:if>
	  <xsl:value-of select=".//*:item"/>
	</span>
  </xsl:template>


  <!-- responsabili per la codifica -->
  <xsl:template name="responsabili">
	<xsl:param name="radix"/>
	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Responsabili di codifica:</xsl:text></b>

	<xsl:copy-of select="$blank"/>
	<xsl:call-template name="doc_resp">
	  <xsl:with-param name="radix" select="$radix"/>
	</xsl:call-template>
  </xsl:template>

  <xsl:template name="doc_resp">
	<xsl:param name="radix"/>

	<span id="docResp">
	  <xsl:choose>

		<xsl:when test="$radix//*:name">
		  <xsl:for-each select="$radix//*:name">
			<xsl:if test="position() &gt; 1">
			  <xsl:copy-of select="$comma"/>
			</xsl:if>
			<xsl:value-of select="."/>
		  </xsl:for-each>
		</xsl:when>

		<xsl:otherwise>
		  <xsl:for-each select="$radix">
			<xsl:if test="position() &gt; 1">
			  <xsl:copy-of select="$comma"/>
			</xsl:if>
			<xsl:value-of select="."/>
		  </xsl:for-each>
		</xsl:otherwise>

	  </xsl:choose>
	</span>
  </xsl:template>


  <!-- un testo che non e` un' opera -->
  <xsl:template name="nao">
	<b><xsl:text>Documento:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docDescr">
	  <xsl:value-of select="//*:opener"/>
	</span>
  </xsl:template>

</xsl:stylesheet>
