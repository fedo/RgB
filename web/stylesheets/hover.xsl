<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : hover.xsl
    Author     : illbagna
    Description: XSL stylesheet for the data retrieval associated to the hover event
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/> 

  <!-- global variables -->
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
           un manoscritto presenta il proprio titolo all'interno di
           <msContents>
           -->
		   <xsl:when test="//*:msContents">
			 <xsl:variable name="location" select="//*:msContents"/>

			 <xsl:apply-templates select="$location//*:title"/>
			 <xsl:copy-of select="$newline"/>
			 <xsl:apply-templates select="$location//*:author"/>
		   </xsl:when>
           <!--
           mentre gli altri documenti presentano titolo ed autore all'interno
           o del tag <sourceDesc>
           -->
           <xsl:when test="//*:sourceDesc//*:title and //*:sourceDesc//*:author">
			 <xsl:variable name="location" select="//*:sourceDesc"/>

			 <xsl:apply-templates select="$location//*:title"/>
			 <xsl:copy-of select="$newline"/>
			 <xsl:apply-templates select="$location//*:author"/>
           </xsl:when>
           <!--
           o del tag <titleStmt>
           -->
           <xsl:when test="//*:titleStmt//*:title and //*:titleStmt//*:author">
			 <xsl:variable name="location" select="//*:titleStmt"/>

			 <xsl:apply-templates select="$location//*:title"/>
			 <xsl:copy-of select="$newline"/>
			 <xsl:apply-templates select="$location//*:author"/>
           </xsl:when>
           <!--
           o in nessuno dei precedenti, se non si tratta di un'opera
           -->
           <xsl:otherwise>
			 <!--<xsl:call-template name="nao"/>-->
			 <b><xsl:text>Documento:</xsl:text></b>
			 <xsl:copy-of select="$blank"/>
			 <span id="docDescr">
			   <xsl:value-of select="//*:opener"/>
			 </span>
           </xsl:otherwise>
        </xsl:choose>

        <!--
        therefore, every document may have the following info
		-->
		<xsl:if test="//*:witList">
		  <xsl:copy-of select="$newline"/>
		  <b><xsl:text>Testimoni:</xsl:text></b>
		  <xsl:copy-of select="$blank"/>
		  <span id="docWit">
			<xsl:apply-templates select="//*:witList"/>
		  </span>
		</xsl:if>

		<xsl:if test="//*:revisionDesc">
		  <xsl:copy-of select="$newline"/>
		  <b><xsl:text>Revisioni:</xsl:text></b>
		  <xsl:copy-of select="$blank"/>
		  <span id="docRev">
			<xsl:apply-templates select="//*:revisionDesc"/>
		  </span>
		</xsl:if>

		<xsl:if test="//*:respStmt">
		  <xsl:copy-of select="$newline"/>
		  <b><xsl:text>Responsabili di codifica:</xsl:text></b>
		  <xsl:copy-of select="$blank"/>
		  <span id="docResp">
			<xsl:apply-templates select="//*:respStmt"/>
		  </span>
		</xsl:if>
     </div>
  </xsl:template>


  <!-- titolo -->
  <xsl:template match="*:title">
	<b><xsl:text>Titolo:</xsl:text></b>
	<xsl:copy-of select="$blank"/>

	<span id="docTitle">
	  <xsl:value-of select="."/>
	</span>
  </xsl:template>


  <!-- autore -->
  <xsl:template match="*:author">
	<b><xsl:text>Autore:</xsl:text></b>
	<xsl:copy-of select="$blank"/>

	<span id="docAuthor">
	  <xsl:value-of select="."/>
	</span>
  </xsl:template>


  <!-- testimoni -->
  <xsl:template match="*:witList">
	<xsl:variable name="witnesses" as="xs:string*">
	  <xsl:sequence select=".//*:witness[@sigil]/attribute(sigil)"/>
	</xsl:variable>

	<xsl:value-of select="$witnesses" separator=", "/>
   </xsl:template>


  <!-- revisori -->
  <xsl:template match="*:revisionDesc">
	<xsl:variable name="revisors" as="item()*">
	  <xsl:sequence select=".//*:date"/>
	  <xsl:sequence select=".//*:name"/>
	  <xsl:sequence select=".//*:item"/>
	</xsl:variable>

	<xsl:value-of select="$revisors" separator=", "/>
  </xsl:template>


  <!-- responsabili per la codifica -->
  <xsl:template match="*:respStmt">
	<xsl:variable name="responsible" as="xs:string*">
	  <xsl:sequence select="./element()"/>
	  <xsl:sequence select="$period"/>
	</xsl:variable>

	<xsl:for-each select="$responsible">
	  <xsl:value-of select="."/>
	  <xsl:copy-of select="$blank"/>
	</xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
