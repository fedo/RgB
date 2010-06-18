<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: preface.xsl
Author: illbagna
Description: XSL stylesheet for the data retrieval associated to the document presentation
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



	  <xsl:apply-templates select="//*:rubric"/>
	  <xsl:apply-templates select="//*:incipit"/>

	  <!-- gli eventuali testimoni -->
	  <xsl:apply-templates select="//*:witList"/>

	  <!-- revisori -->
	  <xsl:apply-templates select="//*:revisionDesc"/>

	  <!-- editori -->
	  <xsl:apply-templates select="//*:editionStmt"/>

	  <!-- collocazione -->
	  <xsl:if test="//*:repository or //*:settlement">
		<xsl:call-template name="collocazione"/>
	  </xsl:if>

	  <!-- supporto -->
	  <xsl:if test="//*:support or //*:condition">
		<xsl:call-template name="supporto"/>
	  </xsl:if>

	  <!-- pubblicazione -->
	  <xsl:apply-templates select="//*:publicationStmt"/>
        
	  <!-- encoding -->
	  <xsl:apply-templates select="//*:encodingDesc"/>
        
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




  <!-- revisori -->
  <xsl:template match="*:revisionDesc">
	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Revisioni:</xsl:text></b>
	<xsl:copy-of select="$blank"/>

	<xsl:variable name="revisors" as="item()*">
	  <xsl:sequence select=".//*:date"/>
	  <xsl:sequence select=".//*:name"/>
	  <xsl:sequence select=".//*:item"/>
	</xsl:variable>

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




  <!-- rubrica -->
  <xsl:template match="*:rubric">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Rubrica:</xsl:text></b>

     <xsl:copy-of select="$blank"/>
     <span id="docRubric">
        <xsl:value-of select="."/>
     </span>
  </xsl:template>




  <!-- incipit -->
  <xsl:template match="*:incipit">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Incipit:</xsl:text></b>

     <xsl:copy-of select="$blank"/>
     <span id="docIncipit">
        <xsl:value-of select="."/>
     </span>
  </xsl:template>




  <!-- collocazione del documento -->
  <xsl:template name="collocazione">
	<!-- repository -->
	<xsl:variable name="loc_rep" as="xs:string*">
	  <xsl:sequence select="//*:repository"/>
	</xsl:variable>

	<!-- region/state -->
	<xsl:variable name="loc_reg" as="xs:string*">
	  <xsl:sequence select="(' (')"/>
	  <xsl:sequence select="//*:settlement"/>
	  <xsl:sequence select="//*:region"/>
	  <xsl:sequence select="(')')"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Collocazione:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docLocat">
	  <xsl:value-of select="$loc_rep" separator=","/>
	  <xsl:value-of select="$loc_reg"/>
	</span>
  </xsl:template>




  <!-- supporto -->
  <xsl:template name="supporto">
	<!-- dimensioni supporto -->
	<xsl:variable name="dimensioni" as="xs:string*">
	  <xsl:sequence select="//*:dimensions//*:height"/>
	  <xsl:sequence select="//*:dimensions//*:width"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Dimensioni:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docDim">
	  <xsl:value-of select="$dimensioni" separator="x"/>
	  <xsl:value-of select="//*:dimensions/@unit"/>
	</span>

	<!-- descrizione -->
	<xsl:variable name="supporto" as="xs:string*">
	  <xsl:sequence select="//*:support"/>
	  <xsl:sequence select="//*:layoutDesc"/>
	  <xsl:sequence select="//*:handDesc"/>
	  <xsl:sequence select="//*:noteStmt"/>
	  <xsl:sequence select="//*:history"/>
	  <xsl:sequence select="//*:condition"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Supporto:</xsl:text></b>
	<xsl:copy-of select="$blank"/>
	<span id="docSupp">
	  <xsl:value-of select="$supporto" separator=" "/>
	</span>
  </xsl:template>




  <!-- pubblicazione -->
  <xsl:template match="*:publicationStmt">
	<xsl:variable name="pubblicazione" as="xs:string*">
	  <xsl:for-each select="./*">
		<xsl:sequence select="."/>
	  </xsl:for-each>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Pubblicazione:</xsl:text></b>

	<xsl:copy-of select="$blank"/>
	<span id="docPub">
	  <xsl:value-of select="$pubblicazione" separator="&#xA;"/>
	</span>
  </xsl:template>




  <!-- note sulla codifica del documento -->
  <xsl:template match="*:encodingDesc">
	<xsl:variable name="encodingdesc" as="xs:string*">
	  <xsl:for-each select="./*">
		<xsl:sequence select="."/>
	  </xsl:for-each>
	  <xsl:sequence select="//*:profileDesc"/>
	</xsl:variable>

	<xsl:copy-of select="$newline"/>
	<b><xsl:text>Codifica del testo:</xsl:text></b>

	<xsl:copy-of select="$blank"/>
	<span id="docEnc">
	  <xsl:value-of select="$encodingdesc" separator="&#xA;"/>
	</span>
  </xsl:template>

</xsl:stylesheet>
