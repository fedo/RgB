<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : hover.xsl
    Author     : illbagna
    Description: XSL stylesheet for the data retrieval associated to the hover event
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:import href="xsl_collection.xsl"/>

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>


  <xsl:template match="/">
     <div>
        <xsl:choose>
           <!--
           un manoscritto presenta il proprio titolo all'interno di
           <msContents>
           -->
		   <xsl:when test="//*:msContents">
			 <xsl:call-template name="titolo">
			   <xsl:with-param name="radix" select="//*:msContents"/>
			 </xsl:call-template>

			 <xsl:call-template name="autore">
			   <xsl:with-param name="radix" select="//*:msContents"/>
			 </xsl:call-template>
		   </xsl:when>
           <!--
           mentre gli altri documenti presentano titolo ed autore all'interno
           o del tag <sourceDesc>
           -->
           <xsl:when test="//sourceDesc//title and //sourceDesc//author">
              <xsl:call-template name="titolo">
                 <xsl:with-param name="radix" select="//sourceDesc"/>
              </xsl:call-template>
              
              <xsl:call-template name="autore">
                 <xsl:with-param name="radix" select="//sourceDesc"/>
              </xsl:call-template>
           </xsl:when>
           <!--
           o del tag <titleStmt>
           -->
           <xsl:when test="//titleStmt//title and //titleStmt//author">
              <xsl:call-template name="titolo">
                 <xsl:with-param name="radix" select="//titleStmt"/>
              </xsl:call-template>

              <xsl:call-template name="autore">
                 <xsl:with-param name="radix" select="//titleStmt"/>
              </xsl:call-template>
           </xsl:when>
           <!--
           o in nessuno dei precedenti, se non si tratta di un'opera
           -->
           <xsl:otherwise>
			 <xsl:call-template name="nao"/>
           </xsl:otherwise>
        </xsl:choose>

        <!--
        therefore, every document may have the following info
        -->
        <xsl:apply-templates select="//*:witList"/>
        <xsl:apply-templates select="//*:revisionDesc"/>
		<xsl:if test="//*:respStmt">
		  <xsl:call-template name="responsabili">
			<xsl:with-param name="radix" select="//*:respStmt"/>
		  </xsl:call-template>
		</xsl:if>
     </div>
  </xsl:template>

</xsl:stylesheet>
