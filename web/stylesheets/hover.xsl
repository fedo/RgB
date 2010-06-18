<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: hover.xsl
Author: illbagna
Description: XSL stylesheet for the data retrieval associated to the mouse hover event associated to the document list
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema">


  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/> 



  <!-- variabili -->
  <xsl:variable name="blank">
     <xsl:text> </xsl:text>
  </xsl:variable>
  <xsl:variable name="newline">
     <br />
  </xsl:variable>
  <xsl:variable name="comma">
     <xsl:text>, </xsl:text>
  </xsl:variable>
  <xsl:variable name="period">
     <xsl:text>.</xsl:text>
  </xsl:variable>



  <!-- templates -->
  <xsl:template match="/">
	<div>
	  <xsl:choose>


		<!--
		il documento codificato e` un'opera
		-->
		<xsl:when test="//element()[((child::*:title) and (child::*:author))]">
		  <!--xsl:variable name="location" select="name(//element()[((child::*:title) and (child::*:author))])"/-->

		  <xsl:variable name="location" as="xs:string*">
			<xsl:for-each select="//element()[((child::*:title) and (child::*:author))]">
			  <xsl:sequence select="name(.)"/> <!-- puo` esserci piu` di un elemento con questi figli -->
			</xsl:for-each>
		  </xsl:variable>


		  <!-- il titolo del testo codificato -->
		  <xsl:apply-templates select="//*:title">
			<xsl:with-param name="location" select="$location[1]"/>
		  </xsl:apply-templates>


		  <!-- l'autore del testo codificato -->
		  <xsl:apply-templates select="//*:author">
			<xsl:with-param name="location" select="$location[1]"/>
		  </xsl:apply-templates>


		  <!-- responsabili di codifica -->
		  <xsl:if test="//element()[((child::*:title) and (child::*:author)) and (child::*:respStmt)]">
			<xsl:copy-of select="$newline"/>

			<b><xsl:text>Responsabili:</xsl:text></b>
			<xsl:copy-of select="$blank"/>

			<span id="docResp">
			  <xsl:apply-templates select="//*:respStmt">
				<xsl:with-param name="location" select="$location[1]"/>
			  </xsl:apply-templates>
			</span>
		  </xsl:if>

		</xsl:when>


		<!--
		un documento codificato non e` un'opera:
		puo` essere una lettera, un quaderno di appunti, ecc
		-->
		<xsl:when test="//*:opener">
		  <xsl:variable name="location" select="name(//element(opener))"/>

		  <b><xsl:text>Documento:</xsl:text></b>
		  <xsl:copy-of select="$blank"/>
		  <span id="docDescr">
			<xsl:value-of select="//*:opener"/>
		  </span>
		</xsl:when>


		<!-- altro -->
		<xsl:otherwise>
		  <xsl:text/>
		</xsl:otherwise>

	  </xsl:choose>



	  <!-- gli eventuali testimoni -->
	  <xsl:apply-templates select="//*:witList"/>



	  <!-- gli eventuali revisori -->
	  <xsl:apply-templates select="//*:revisionDesc"/>



	  <!-- l'edizione -->
	  <xsl:apply-templates select="//*:editionStmt"/>

	</div>
  </xsl:template>




  <!-- titolo -->
  <xsl:template match="*:title">
	<xsl:param name="location"/>

	<xsl:if test="ancestor::node()[contains(name(), $location)]">
	  <b><xsl:text>Titolo:</xsl:text></b>
	  <xsl:copy-of select="$blank"/>
	  <span id="docTitle">
		<xsl:value-of select="."/>
	  </span>
	</xsl:if>

  </xsl:template>




  <!-- autore -->
  <xsl:template match="*:author">
	<xsl:param name="location"/>

	<xsl:if test="ancestor::node()[contains(name(), $location)]">
	  <xsl:copy-of select="$newline"/>
	  <b><xsl:text>Autore:</xsl:text></b>
	  <xsl:copy-of select="$blank"/>
	  <span id="docAuthor">
		<xsl:value-of select="."/>
	  </span>
	</xsl:if>

  </xsl:template>




  <!-- responsabili di codifica -->
  <xsl:template match="*:respStmt">
	<xsl:param name="location"/>

	<xsl:choose>

	  <xsl:when test="ancestor::node()[contains(name(), $location)]">
		<!-- spacer -->
		<xsl:if test="position() &gt; 1">
		  <xsl:copy-of select="$blank"/>
		</xsl:if>

		<xsl:for-each select="*">
		  <xsl:value-of select="."/>
		  <xsl:choose>
			<xsl:when test="position() != last()">
			  <xsl:value-of select="$blank"/>
			</xsl:when>
			<xsl:otherwise>
			  <xsl:value-of select="$period"/>
			</xsl:otherwise>
		  </xsl:choose>
		</xsl:for-each>
	  </xsl:when>

	  <xsl:when test="not(//*:revisionDesc//node())">
		<!-- spacer -->
		<xsl:if test="position() &gt; 1">
		  <xsl:copy-of select="$blank"/>
		</xsl:if>

		<xsl:for-each select="*">
		  <xsl:value-of select="."/>
		  <xsl:choose>
			<xsl:when test="position() != last()">
			  <xsl:value-of select="$blank"/>
			</xsl:when>
			<xsl:otherwise>
			  <xsl:value-of select="$period"/>
			</xsl:otherwise>
		  </xsl:choose>
		</xsl:for-each>
	  </xsl:when>

	</xsl:choose>

  </xsl:template>




  <!-- testimoni -->
  <xsl:template match="*:witList">
	<xsl:variable name="witnesses" as="xs:string*">
	  <xsl:sequence select=".//*:witness[@sigil and not(contains(@missing, 'true'))]/attribute(sigil)"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Testimoni:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docWit">
	  <xsl:value-of select="$witnesses" separator=", "/>
	</span>
  </xsl:template>




  <!-- revisori -->
  <xsl:template match="*:revisionDesc">
	<xsl:variable name="revisors" as="item()*">
	  <xsl:sequence select=".//*:date"/>
	  <xsl:sequence select=".//*:name"/>
	  <xsl:sequence select=".//*:item"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Revisioni:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docRev">
	  <xsl:value-of select="$revisors" separator=", "/>
	</span>
  </xsl:template>




  <!-- editori -->
  <xsl:template match="*:editionStmt">
	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Edizione:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docEdit">
	  <xsl:for-each select="*">
		<xsl:value-of select="."/>
		<xsl:choose>
		  <xsl:when test="position() != last()">
			<xsl:value-of select="$blank"/>
		  </xsl:when>
		  <xsl:otherwise>
			<xsl:value-of select="$period"/>
		  </xsl:otherwise>
		</xsl:choose>
	  </xsl:for-each>
	</span>
  </xsl:template>

</xsl:stylesheet>
