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

mvn -version
cd cool_project
mvn compile
mvn test
