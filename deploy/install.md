Настройка сервера
=================

Ubuntu 18.04

<div class="code">

ssh-copy-id root@server ssh root@ser apt update apt dist-upgrade apt
install python

</div>

Настройка ansible
=================

Заполнить файл `hosts` следующим образом

<div class="code">

\[lcp\] &lt;server&gt;

</div>

Проверка работоспособности

<div class="code">

ansible -i hosts lcp -m ping

</div>

Ответ

<div class="code">

&lt;server&gt; | SUCCESS =&gt; { "changed": false, "ping": "pong" }

</div>

Установить hostname на сервере

<div class="code">

ansible -i hosts lcp -m hostname -a name=runcity-kzn2018

</div>

Ответ

<div class="code">

&lt;server&gt; | SUCCESS =&gt; { "ansible~facts~": { "ansible~domain~":
"com", "ansible~fqdn~": "bjjjournal.com", "ansible~hostname~":
"runcity-kzn2018", "ansible~nodename~": "runcity-kzn2018" }, "changed":
true, "name": "runcity-kzn2018" }

</div>

Запуск ansible
==============

<div class="code">

ansible-playbook -i hosts lcp.yml

</div>
