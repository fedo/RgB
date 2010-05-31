<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : xsl_collection.xsl
    Author     : illbagna
    Description: collection of XSLT stylesheets
-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8"/>

  <xsl:variable name="newline">
     <br />
  </xsl:variable>

  <xsl:template match="/">
     <!--
     <html>
        <head>
	 </head>

        <body>
           <div>-->
              <xsl:choose>
                 <!--
                 un manoscritto presenta il proprio titolo all'interno di
                 <msContents>
                 -->
                 <xsl:when test="//msContents">
                    <xsl:apply-templates select="msContents"/>
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
                    <b><xsl:text>Documento:</xsl:text></b>
                    <span>
                       <xsl:copy-of select="$newline"/>
                       <xsl:value-of select="//opener"/>
                    </span>
                 </xsl:otherwise>
              </xsl:choose>

              <!--
              therefore, every document may have the following info
              -->
              <xsl:apply-templates select="//witList"/>
              <xsl:apply-templates select="//revisionDesc"/>
              <xsl:if test="//respStmt">
                 <xsl:copy-of select="$newline"/>
                 <b><xsl:text>Responsabili di codifica:</xsl:text></b>
              </xsl:if>
              <xsl:apply-templates select="//respStmt"/>
          <!-- </div>
           
        </body>
     </html>-->
  </xsl:template>


  <!-- titolo del documento -->
  <xsl:template name="titolo">
     <xsl:param name="radix"/>
     <b><xsl:text>Titolo:</xsl:text></b>
     <span>
        <xsl:copy-of select="$newline"/>
        <xsl:value-of select="$radix//title"/>
     </span>
  </xsl:template>
  

  <!-- autore del documento -->
  <xsl:template name="autore">
     <xsl:param name="radix"/>
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Autore:</xsl:text></b>
     <span>
        <xsl:copy-of select="$newline"/>
        <xsl:value-of select="$radix//author"/>
     </span>
  </xsl:template>

  <!-- testimoni -->
  <xsl:template match="witList">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Testimoni:</xsl:text></b>
     <span>
        <xsl:for-each select=".//witness[@sigil]">
           <xsl:copy-of select="$newline"/>
           <xsl:value-of select="@sigil"/>
        </xsl:for-each>
     </span>
  </xsl:template>

  <!-- revisori -->
  <!-- <xsl:template match="revisionDesc">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Revisioni:</xsl:text></b>
     <span>
        <xsl:copy-of select="$newline"/>
        <xsl:template match="date">
           <xsl:value-of select=".//date"/>
        </xsl:template>
        <xsl:copy-of select="$newline"/>
        <xsl:template match="name">
           <xsl:value-of select=".//name"/>
        </xsl:template>
        <xsl:copy-of select="$newline"/>
        <xsl:template match="item">
           <xsl:value-of select=".//item"/>
        </xsl:template>
     </span>
  </xsl:template> -->

  <!-- responsabili per la codifica -->
  <xsl:template match="respStmt">
     <span>
        <xsl:choose>
           <xsl:when test=".//name">
              <xsl:for-each select=".//name">
                 <xsl:copy-of select="$newline"/>
                 <xsl:value-of select="."/>
              </xsl:for-each>
           </xsl:when>
           <xsl:otherwise>
              <xsl:for-each select="./*">
                 <xsl:copy-of select="$newline"/>
                 <xsl:value-of select="."/>
              </xsl:for-each>
           </xsl:otherwise>
        </xsl:choose>
     </span>
  </xsl:template>


</xsl:stylesheet>
