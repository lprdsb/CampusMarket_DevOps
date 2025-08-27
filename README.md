开发记得先自己开一个分支，避免把原本好的代码弄坏
```sh
git switch -c code-dev-[你的名字]
```

你push到自己的分支之后在群里说一声，我来合并。

```sh
docker image pull lprlpr/test:123
docker container run -p 11451:11451 -it lprlpr/test:123
```
