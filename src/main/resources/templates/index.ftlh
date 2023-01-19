<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pegas-Agro</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
            integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<header>
    <h2>Расчет пройденного пути по координатам</h2>
    <p>По данным из приложенного файла пройденный путь составляет <b>${distanceKM! 0}</b>км.
        Количество строк в файле = <b>${rowCounterInFile! 0}</b>.
        <#if countInvalidStrings != "0">
            Строки, полученные из файла:
            <#list map as key, value>
                <b>${value}</b>  под № <b>${key}</b>, </#list>
        </#if> не проходят по стандарту описания протокола NMEA 0183. ВЫ можете загрузить файл и рассчитать путь нажав
        на кнопку ниже.</p>
</header>
<button class="tryBtn">Рассчитать путь</button>
<script>
    let tryButton = document.querySelector('.tryBtn')

    tryButton.addEventListener('click', () => {
        tryButton.remove()

        let wrapperForCompute = document.createElement('div')
        wrapperForCompute.classList.add('wrapper')

        let formLoadFile = document.createElement('form')
        formLoadFile.id = 'form'
        formLoadFile.enctype = 'multipart/form-data'

        let labelWrap = document.createElement('label')
        labelWrap.classList.add('label-wrap')

        let spanFileName = document.createElement('span')
        spanFileName.type = 'text'
        spanFileName.classList.add('name')

        let chooseFile = document.createElement('span')
        chooseFile.classList.add('choose-btn')
        chooseFile.innerHTML = 'Выбрать файл'

        let inputFileName = document.createElement('input')
        inputFileName.id = 'file'
        inputFileName.type = 'file'
        inputFileName.accept = 'text/*'

        labelWrap.append(spanFileName, inputFileName, chooseFile)

        let submitBtn = document.createElement('input')
        submitBtn.value = 'Выполнить расчет'
        submitBtn.type = 'submit'

        formLoadFile.append(labelWrap, submitBtn)

        let result = document.createElement('p')
        result.classList.add('result')
        result.innerText = 'Пройденный путь ='

        wrapperForCompute.append(formLoadFile, result)

        document.querySelector('body').append(wrapperForCompute)

        $('input[type=file]').on('change', function () {
            let file = this.files[0]
            console.log(file.name)
            $(this).closest('.label-wrap').find('.name').html(file.name)
        });

        $("#form").on('submit', function (e) {
            e.preventDefault()
            if ($('#file')[0].value === '') {
                spanFileName.classList.add('error')
                alert('Выберите файл')
                return
            }
            result.innerText = 'Пройденный путь ='
            let formData = new FormData()
            formData.append('file', $('#file')[0].files[0])
            $.ajax({
                url: '/loadAndReadFile',
                method: 'post',
                contentType: false,
                processData: false,
                dataType: 'text',
                data: formData,
                success: function (data, textStatus, jqXHR) {
                    result.innerText += jqXHR.responseText + 'км'
                    spanFileName.classList.remove('error')
                    document.querySelector('.name').innerHTML = ''
                }
            })
        });
    })
</script>
</body>
</html>

