function Update-Environment {
    $locations = 'HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
                 'HKCU:\Environment'

    $locations | ForEach-Object {
        $k = Get-Item $_
        $k.GetValueNames() | ForEach-Object {
            $name  = $_
            $value = $k.GetValue($_)

            if ($userLocation -and $name -ieq 'PATH') {
                Env:\Path += ";$value"
            } else {
                Set-Item -Path Env:\$name -Value $value
            }
        }

        $userLocation = $true
    }
}

Update-Environment

cd cool_project\client
mvn assembly:single
cd ..\..
move cool_project/client/target/client-jar-with-dependencies.jar .
move client-jar-with-dependencies.jar client%TRAVIS_TAG%-win32.jar
