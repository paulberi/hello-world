<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayRequiredFields=false displayWide=false showAnotherWayIfPresent=true>
<!DOCTYPE html>
<html class="${properties.kcHtmlClass!}" lang="${(locale.currentLanguageTag)!'en'}">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>
    <title>${msg("loginTitle",(realm.displayName!''))}</title>
    <script>
        let contentLoaded = new Promise((resolve) =>
            window.addEventListener("DOMContentLoaded", resolve));

        let realm='${(realm.name)!""}';
        let clientId='${(client.clientId)!""}';

        if (realm !== '' && clientId !== '') {
            let url = "/config/app-logininfo/"+realm+"/"+clientId;

            let loginInfoLoaded = fetch(url);

            Promise.all([loginInfoLoaded, contentLoaded])
                .then(([loginInfoResult, contentLoadedResult]) => loginInfoResult.json())
                .then((loginInfo) => {
                    if (loginInfo.name) {
                        var productTitle = document.getElementById('product-name');
                        productTitle.innerText = loginInfo.name;
                    }

                    if (loginInfo.description) {
                        var productDescription = document.getElementById('product-description');
                        productDescription.innerText = loginInfo.description;
                    }

                    if (loginInfo.cssUrl) {
                        var header = document.getElementsByTagName('head')[0];
                        var link = document.createElement('link');
                        link.setAttribute('rel', 'stylesheet');
                        link.setAttribute('type', 'text/css');
                        link.setAttribute('href', loginInfo.cssUrl);
                        header.appendChild(link);
                    }
                });
        }
    </script>

    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <#if properties.stylesCommon?has_content>
        <#list properties.stylesCommon?split(' ') as style>
            <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>

<body class="${properties.kcBodyClass!}">
    <div class="${properties.kcLoginClass!}">
        <div id="kc-header" class="${properties.kcHeaderClass!}">
            <span lang="sv" id="header-text">Insikter som ritar om kartan</span>
            <div id="kc-header-wrapper" class="${properties.kcHeaderWrapperClass!}">${kcSanitize(msg("loginTitleHtml",(realm.displayNameHtml!'')))?no_esc}</div>
        </div>
        <div id="main-content">
            <div class="column">
                <h1 lang="sv" id="product-title"><span>Välkommen till</span> <span id="product-name">${(realm.displayName!'')}</span></h1>
                <p lang="sv" id="product-description"></p>
                <p lang="sv">Upplever du problem med tjänsten? Kontakta vår support på helgfri måndag-fredag mellan kl. 08-17 på <span style="white-space:nowrap;">010-121 81 00</span> eller via e-post till <a href="mailto:support@metria.se">support@metria.se</a></p>
            </div>
            <div class="column">
                <div class="${properties.kcFormCardClass!} <#if displayWide>${properties.kcFormCardAccountClass!}</#if>">
                    <header class="${properties.kcFormHeaderClass!}">
                        <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
                            <div class="${properties.kcLocaleMainClass!}" id="kc-locale">
                                <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                                    <div id="kc-locale-dropdown" class="${properties.kcLocaleDropDownClass!}">
                                        <a href="#" id="kc-current-locale-link">${locale.current}</a>
                                        <ul class="${properties.kcLocaleListClass!}">
                                            <#list locale.supported as l>
                                                <li class="${properties.kcLocaleListItemClass!}">
                                                    <a class="${properties.kcLocaleItemClass!}" href="${l.url}">${l.label}</a>
                                                </li>
                                            </#list>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </#if>
                        <#if !(auth?has_content && auth.showUsername() && !auth.showResetCredentials())>
                        <#else>
                            <#if displayRequiredFields>
                                <div class="${properties.kcContentWrapperClass!}">
                                    <div class="${properties.kcLabelWrapperClass!} subtitle">
                                        <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
                                    </div>
                                    <div class="col-md-10">
                                        <#nested "show-username">
                                        <div id="kc-username" class="${properties.kcFormGroupClass!}">
                                            <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                                            <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg("restartLoginTooltip")}">
                                                <div class="kc-login-tooltip">
                                                    <i class="${properties.kcResetFlowIcon!}"></i>
                                                    <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                                                </div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            <#else>
                                <#nested "show-username">
                                <div id="kc-username" class="${properties.kcFormGroupClass!}">
                                    <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                                    <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg("restartLoginTooltip")}">
                                        <div class="kc-login-tooltip">
                                            <i class="${properties.kcResetFlowIcon!}"></i>
                                            <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                                        </div>
                                    </a>
                                </div>
                            </#if>
                        </#if>
                    </header>
                    <div id="kc-content">
                        <div id="kc-content-wrapper">

                            <#-- App-initiated actions should not see warning messages about the need to complete the action -->
                            <#-- during login.                                                                               -->
                            <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                                <div class="alert-${message.type} ${properties.kcAlertClass!} pf-m-<#if message.type = 'error'>danger<#else>${message.type}</#if>">
                                    <div class="pf-c-alert__icon">
                                        <#if message.type = 'success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                                        <#if message.type = 'warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                                        <#if message.type = 'error'><span class="${properties.kcFeedbackErrorIcon!}"></span></#if>
                                        <#if message.type = 'info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                                    </div>
                                    <span class="${properties.kcAlertTitleClass!}">${kcSanitize(message.summary)?no_esc}</span>
                                </div>
                            </#if>

                            <#nested "form">

                            <#if auth?has_content && auth.showTryAnotherWayLink()>
                                <form id="kc-select-try-another-way-form" action="${url.loginAction}" method="post">                                    <div <#if displayWide>class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}"</#if>
                                        <div class="${properties.kcFormGroupClass!}">
                                            <input type="hidden" name="tryAnotherWay" value="on" />
                                            <a href="#" id="try-another-way" onclick="document.forms['kc-select-try-another-way-form'].submit();return false;">${msg("doTryAnotherWay")}</a>
                                        </div>
                                </form>
                            </#if>

                            <#nested "socialProviders">

                            <#if displayInfo>
                                <div id="kc-info" class="${properties.kcSignUpClass!}">
                                    <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                                        <#nested "info">
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div lang="sv" id="marknadsforing-container">
            <h1 id="marknadsforing-title">Fler tjänster från Metria</h1>
            <div id="marknadsforing-content">
                <div id="sesverige">
                    <div id="sesverige-image" class="marknadsforing-image"></div>
                    <div class="marknadsforing-text">
                        <h2 class="marknadsforing-product-title">Metria SeSverige</h2>
                        <p class="marknadsforing-product-description">
                            SeSverige är det verktyg du behöver för att enkelt och flexibelt planera och 
                            e-handla kartor eller söka information om fastigheter. Tjänsten finns såväl 
                            avtalsfritt som i abonnemangsform med grund- eller pro-nivå.
                        </p>
                        <p class="marknadsforing-link"><a href="https://metria.se/produkter-tjanster/geodatatjanster/sesverige/">Läs mer om SeSverige</a></p>
                    </div>
                </div>
                <div id="fsok">
                    <div id="fsok-image" class="marknadsforing-image"></div>
                    <div class="marknadsforing-text">
                        <h2 class="marknadsforing-product-title">FastighetSök</h2>
                        <p class="marknadsforing-product-description">
                            Hitta dagsaktuella uppgifter direkt från fastighetsregistret.
                            På så sätt har du alltid tillgång till den senaste informationen om till exempel ägare,
                            inteckningar och rättigheter, samt taxeringsuppgifter såsom boyta och byggnadsår.
                        </p>
                        <p class="marknadsforing-link"><a href="https://www.metria.se">Till tjänsten FastighetSök</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <footer>
        <div id="footer-left" class="footer-body">
            <h1 id="footer-title">Metria AB</h1>
            <p>
                010-121 81 00 <br>
                801 83 Gävle <br>
                Sverige
            </p>
            <p>
                <a class="footer-link" href="https://www.metria.se">www.metria.se</a>
                <br>
                <a class="footer-link" href="mailto:info@metria.se">info@metria.se</a>
                <br>
            </p>
        </div>
        <div id="footer-right">
            <a href="https://www.metria.se">
                <img src="${url.resourcesPath}/img/metria-logo-text.png" alt="Metrias logotyp">
            </a>
        </div>
    </footer>
</body>
</html>
</#macro>
