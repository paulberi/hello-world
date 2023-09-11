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
        let language="${(locale.currentLanguageTag)!'en'}"

        if (realm !== '' && clientId !== '') {
            let url = "/config/app-logininfo/"+realm+"/"+clientId;

            let loginInfoLoaded = fetch(url);

            Promise.all([loginInfoLoaded, contentLoaded])
                .then(([loginInfoResult, contentLoadedResult]) => loginInfoResult.json())
                .then((loginInfo) => {
                    if (loginInfo) {
                        if (loginInfo.name) {
                            var headerTitle = document.getElementById('product-name-header');
                            var productTitle = document.getElementById('product-name');
                            var footerTitle = document.getElementById('product-name-footer');
                            headerTitle.innerText = loginInfo.name;
                            productTitle.innerText = loginInfo.name;
                            footerTitle.innerText = loginInfo.name;
                        }

                        var productDescription = document.getElementById('product-description');
                        productDescription.innerText = loginInfo["description-" + language] || "";


                        var passwordPolicy = document.getElementById('custom-password-information-text');
                        if (passwordPolicy) {
                            passwordPolicy.innerText = loginInfo["password-policy-" + language] || "";
                        }

                        if (loginInfo.cssUrl) {
                            var header = document.getElementsByTagName('head')[0];
                            var link = document.createElement('link');
                            link.setAttribute('rel', 'stylesheet');
                            link.setAttribute('type', 'text/css');
                            link.setAttribute('href', loginInfo.cssUrl);
                            header.appendChild(link);
                        }
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
            <a id="kc-header-icon" href="">
                <img src="${url.resourcesPath}/img/metria_logo_white.svg" alt="Metrias logotyp">
            </a>
            <h1 lang="sv" id="kc-header-title"><span id="product-name-header">${(realm.displayName!'')}</span></h1>            <div id="kc-header-wrapper" class="${properties.kcHeaderWrapperClass!}">${kcSanitize(msg("loginTitleHtml",(realm.displayNameHtml!'')))?no_esc}</div>
        </div>
        <div id="main-content">
            <div class="column">
                <h1 lang="sv" id="product-title"><span>${msg("customWelcomeTo")}</span> <span id="product-name">${(realm.displayName!'')}!</span></h1>
                <p lang="sv" id="product-description"></p>
                <img id="product-image" src="${url.resourcesPath}/img/welcome.svg" alt="Metrias välkomstbild">
            <#--<p lang="sv">Upplever du problem med tjänsten? Kontakta vår support på helgfri måndag-fredag mellan kl. 08-17 på <span style="white-space:nowrap;">010-121 81 00</span> eller via e-post till <a href="mailto:support@metria.se">support@metria.se</a></p>-->            </div>
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
                                            <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg('restartLoginTooltip')}">
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
                                    <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg('restartLoginTooltip')}">
                                        <div class="kc-login-tooltip">
                                            <i class="${properties.kcResetFlowIcon!}"></i>
                                            <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                                        </div>
                                    </a>
                                </div>
                            </#if>
                        </#if>                 
                        <h1 id="kc-page-title"><#nested "header"></h1>
                    </header>
                    <div id="kc-content">
                        <div id="kc-content-wrapper">

                            <#-- App-initiated actions should not see warning messages about the need to complete the action -->
                            <#-- during login.                                                                               -->
                            <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                                <div class="alert-${message.type} ${properties.kcAlertClass!} pf-m-<#if message.type = 'error'>danger<#else>${message.type}</#if>">
                                    <div id="custom-alert-icon-wrapper" class="pf-c-alert__icon">
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

                            <#if displayInfo>
                                <div id="kc-info" class="${properties.kcSignUpClass!}">
                                    <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                                        <#nested "info">
                                    </div>
                                </div>
                            </#if>

                            <#nested "socialProviders">
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <footer>
        <div id="footer-left" class="footer-body">
            <h1 lang="sv" id="footer-title"><span id="product-name-footer">${(realm.displayName!'')}</span></h1>
            <p>
                ${msg("customFooterDescription")}
            </p>
            <p id="footer-links">
                <a class="footer-link" href="tel:0101218100">010-121 81 00</a>
                <br>
                <a class="footer-link" href="mailto:support@metria.se">support@metria.se</a>
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