<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : preface.xsl
    Author     : illbagna
    Description: XSL stylesheet for the data retrieval associated to the document presentation
-->

<xsl:stylesheet version="2.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:fn="http://www.w3.org/2006/xpath-function">

  <xsl:import href="xsl_collection.xsl"/>

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8"/>

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

			  <xsl:apply-templates select="//*:msDesc"/>
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
        in aggiunta, ogni documento potrebbe contenere le seguenti informazioni
        -->
        <xsl:apply-templates select="//*:rubric"/>
        <xsl:apply-templates select="//*:incipit"/>
        <xsl:apply-templates select="//*:editionStmt"/>

		<!-- collocazione del documento -->
		<xsl:if test="//*:repository or //*:settlement">
		  <xsl:copy-of select="$newline"/>
		  <b><xsl:text>Collocazione:</xsl:text></b>

		  <xsl:copy-of select="$blank"/>
		  <span id="docLocat">

			<xsl:choose>
			  <xsl:when test="//*:repository and //*:settlement">
				<xsl:apply-templates select="//*:repository"/>
				<xsl:copy-of select="$comma"/>
				<xsl:apply-templates select="//*:settlement"/>
			  </xsl:when>
			  <xsl:otherwise>
				<xsl:apply-templates select="//*:repository"/>
				<xsl:apply-templates select="//*:settlement"/>
			  </xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
			  <xsl:when test="//*:region or //*:country">
				<xsl:text> (</xsl:text>

				<xsl:choose>
				  <xsl:when test="//*:region and //*:country">
					<xsl:apply-templates select="//*:region"/>
					<xsl:copy-of select="$comma"/>
					<xsl:apply-templates select="//*:country"/>
					<xsl:text>) </xsl:text>
				  </xsl:when>
				  <xsl:otherwise>
					<xsl:apply-templates select="//*:region"/>
					<xsl:apply-templates select="//*:country"/>
					<xsl:text>) </xsl:text>
				  </xsl:otherwise>
				</xsl:choose>
			  </xsl:when>
			</xsl:choose>

			<xsl:apply-templates select="//*:altIdentifier"/>
		  </span>
		</xsl:if>

		<!-- note sul documento-->
		<xsl:if test="//*:supportDesc">
		  <xsl:copy-of select="$newline"/>
		  <b><xsl:text>Descrizione / Note:</xsl:text></b>
		  <xsl:copy-of select="$blank"/>
		  <span id="docNote">
			<xsl:apply-templates select="//*:supportDesc"/>
			<xsl:copy-of select="$newline"/>
			<xsl:apply-templates select="//*:layoutDesc"/>
			<xsl:copy-of select="$newline"/>
			<xsl:apply-templates select="//*:handDesc"/>
			<xsl:copy-of select="$newline"/>
			<xsl:apply-templates select="//*:noteStmt"/>
			<xsl:copy-of select="$newline"/>
			<xsl:apply-templates select="//*:history"/>
		  </span>
		</xsl:if>


        <xsl:apply-templates select="//*:witList"/>

        <xsl:apply-templates select="//*:revisionDesc"/>
        
		<xsl:if test="//*:respStmt">
		  <xsl:call-template name="responsabili">
			<xsl:with-param name="radix" select="//*:respStmt"/>
		  </xsl:call-template>
		</xsl:if>
        
        <xsl:apply-templates select="//*:publicationStmt"/>
        
        <xsl:apply-templates select="//*:encodingDesc"/>
        
     </div>
  </xsl:template>


  <!-- responsabili per le traduzioni -->
  <xsl:template match="//*:msDesc">
     <xsl:if test=".//*:name or .//*:persName or .//*:respStmt">
        <xsl:copy-of select="$newline"/>
        <b><xsl:text>Traduzione:</xsl:text></b>

        <xsl:copy-of select="$blank"/>
        <span id="docTrad">
		  <xsl:choose>
			<xsl:when test=".//*:name">
			  <xsl:value-of select=".//*:name"/>
			</xsl:when>
			<xsl:otherwise>
			  <xsl:choose>
				<xsl:when test=".//*:respStmt">
				  <xsl:value-of select=".//*:respStmt"/>
				</xsl:when>
				<xsl:otherwise>
				  <xsl:value-of select=".//*:persName"/>
				</xsl:otherwise>
			  </xsl:choose>
			</xsl:otherwise>
		  </xsl:choose>
        </span>
     </xsl:if>
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


  <!-- edizione -->
  <xsl:template match="*:editionStmt">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Edizione:</xsl:text></b>

     <xsl:copy-of select="$blank"/>
     <span id="docEdition">
	   <xsl:for-each select="*">
           <xsl:if test="position() &gt; 1">
              <xsl:copy-of select="$newline"/>
           </xsl:if>
           <xsl:value-of select="."/>
        </xsl:for-each>
     </span>
  </xsl:template>


  <!-- note sulla codifica del documento -->
  <xsl:template match="*:encodingDesc">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Codifica del testo:</xsl:text></b>

     <xsl:copy-of select="$blank"/>
     <span id="docEnc">
        <xsl:for-each select=".//*:p">
           <xsl:if test="position() &gt; 1">
              <xsl:copy-of select="$newline"/>
           </xsl:if>
           <xsl:value-of select="."/>
        </xsl:for-each>
     </span>
  </xsl:template>


  <!-- pubblicazione -->
  <xsl:template match="*:publicationStmt">
     <xsl:copy-of select="$newline"/>
     <b><xsl:text>Pubblicazione:</xsl:text></b>

     <xsl:copy-of select="$blank"/>
     <span id="docPub">
        <xsl:for-each select="*">
           <xsl:if test="position() &gt; 1">
              <xsl:copy-of select="$newline"/>
           </xsl:if>
           <xsl:value-of select="."/>
        </xsl:for-each>
     </span>
  </xsl:template>

  <!--
  <xsl:template match="*:layoutDesc">
	<xsl:copy-of select="$newline"/>
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="*:handDesc">
	<xsl:copy-of select="$newline"/>
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="*:notesStmt">
	<xsl:copy-of select="$newline"/>
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="*:history">
	<xsl:copy-of select="$newline"/>
	<xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="//*:repository">
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="//*:settlement">
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="//*:region">
	<xsl:value-of select="."/>
  </xsl:template>


  <xsl:template match="//*:country">
	<xsl:value-of select="."/>
  </xsl:template>
-->


  <xsl:template match="//*:altIdentifier">
	<xsl:copy-of select="$newline"/>
	<xsl:value-of select="."/>
  </xsl:template>

</xsl:stylesheet>
