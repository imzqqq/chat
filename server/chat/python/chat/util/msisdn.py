import phonenumbers

from chat.api.errors import SynapseError


def phone_number_to_msisdn(country: str, number: str) -> str:
    """
    Takes an ISO-3166-1 2 letter country code and phone number and
    returns an msisdn representing the canonical version of that
    phone number.
    Args:
        country: ISO-3166-1 2 letter country code
        number: Phone number in a national or international format

    Returns:
        The canonical form of the phone number, as an msisdn
    Raises:
        SynapseError if the number could not be parsed.
    """
    try:
        phoneNumber = phonenumbers.parse(number, country)
    except phonenumbers.NumberParseException:
        raise SynapseError(400, "Unable to parse phone number")
    return phonenumbers.format_number(phoneNumber, phonenumbers.PhoneNumberFormat.E164)[
        1:
    ]
