Настройка сервера
=================

Ubuntu 18.04

```bash
ssh-copy-id root@server 
ssh root@server 
apt update 
apt dist-upgrade 
apt install python2.7
ln -s /usr/bin/python2.7 /usr/bin/python
```

Настройка ansible
=================

Заполнить файл `hosts` следующим образом

```
[lcp] 
<server>
```

Например,

```
[lcp]
1.2.3.4
10.20.30.40 ansible_ssh_user=root
```

Проверка работоспособности

```bash
ansible -i hosts lcp -m ping
```

Ответ

```
<server> | SUCCESS => { "changed": false, "ping": "pong" }
```

Установить hostname на сервере

```
ansible -i hosts lcp -m hostname -a name=runcity-kzn2018
```

Ответ

```
<server> | CHANGED => {
    "ansible_facts": {
        "ansible_domain": "com",
        "ansible_fqdn": "gperfume.com",
        "ansible_hostname": "runcity-ulsk2019",
        "ansible_nodename": "runcity-ulsk2019",
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": true,
    "name": "runcity-ulsk2019"
}
```

Запуск ansible
==============

```bash
ansible-playbook -i hosts lcp.yml
```
